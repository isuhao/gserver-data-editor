package com.gserver.data.editor.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gserver.common.repository.MissionRepository2;
import com.gserver.data.editor.TableEntity;
import com.gserver.data.editor.dto.TableArrayRule;
import com.gserver.data.editor.dto.TableTitle;
import com.gserver.data.editor.dto.Tree;
import com.gserver.data.editor.interceptor.LockTableInterceptor;
import com.gserver.data.editor.service.TablesService;
import com.gserver.data.editor.util.BaseQEntity;
import com.gserver.data.editor.util.ClassUtils;
import com.gserver.data.editor.util.EntityUtils;
import com.gserver.data.editor.util.FileToEntityUtils;
import com.gserver.data.editor.util.ReflectionUtils;
import com.gserver.data.editor.util.Servlets;

@Controller
public class EditorController {

	@Autowired
	TablesService tablesService;
	@Autowired
	MissionRepository2 missionRepository2;

	/**
	 * 首页跳转
	 * 
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		
		return "index";
	}

	/**
	 * 获取左侧树形目录结构
	 */
	@ResponseBody
	@RequestMapping(value = "/getTree", method = { RequestMethod.GET, RequestMethod.POST })
	public List<Tree> getTree(String id) {
		List<Tree> list = tablesService.getTreeLayer(id);
		return list;
	}

	/**
	 * 表头信息
	 * 
	 * @param model
	 * @param table
	 * @return
	 */
	@RequestMapping(value = "/{table}/open", method = RequestMethod.GET)
	public String openTable(Model model, @PathVariable String table) {
		model.addAttribute("tablename", table);
		Class<TableEntity> beanClass = EntityUtils.getMappedClass(table);
		List<TableTitle> titles = tablesService.getSimpleTitles(table);
		for (TableTitle title : titles) {
			tablesService.setEditor(title, beanClass, table, title.getName());
		}
		model.addAttribute("columns", titles);
		model.addAttribute("lockTableIP", LockTableInterceptor.lockTableIP(table));
		return "table";
	}

	/**
	 * 加载表数据
	 * 
	 * @param tablename
	 * @param page
	 * @param rows
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{tablename}/loaddata", method = RequestMethod.POST)
	public BaseQEntity<TableEntity> loadData(@PathVariable String tablename, @RequestParam(required = true) int page, @RequestParam(required = true) int rows, HttpServletRequest request) {
		Class<TableEntity> beanPrototype = EntityUtils.getMappedClass(tablename);
		BaseQEntity<TableEntity> b = BaseQEntity.<TableEntity> newInstance().setCurrentPage(page).setPerPageRows(rows);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		tablesService.getPage(beanPrototype, b, searchParams);
		return b;
	}

	/**
	 * 保存表数据
	 * 
	 * @param model
	 * @param table
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{table}/save", method = { RequestMethod.POST })
	public Map<String, Object> saveRow(Model model, @PathVariable TableEntity table) {
		Map<String, Object> map = Maps.newHashMapWithExpectedSize(1);
		tablesService.insertData(table);
		map.put("code", table.getCode());
		return map;
	}

	/**
	 * 更新表数据
	 * 
	 * @param model
	 * @param table
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{table}/update", method = RequestMethod.POST)
	public Map<String, Object> updateRow(Model model, @PathVariable TableEntity table) {
		Map<String, Object> map = Maps.newHashMapWithExpectedSize(1);
		tablesService.updateData(table);
		map.put("code", table.getCode());
		return map;
	}

	/**
	 * 删除表数据
	 * 
	 * @param model
	 * @param tablename
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{tablename}/delete", method = { RequestMethod.POST })
	public Map<String, Object> deleteRow(Model model, @PathVariable String tablename, @RequestParam(required = true) Long id) {
		Map<String, Object> map = Maps.newHashMapWithExpectedSize(1);
		if (id != null) {
			Class<TableEntity> beanClass = EntityUtils.getMappedClass(tablename);
			TableEntity t = tablesService.getDataById(beanClass, id);
			// // 验证关联，考虑数组的情况。
			boolean isDeletable = tablesService.isDeletable(tablename, t);
			if (isDeletable) {
				tablesService.deleteDataById(beanClass, t.getCode());
				map.put("isError", false);
				return map;
			}
		}
		map.put("isError", true);
		map.put("errorMsg", "存在关联，不能删除！");
		return map;
	}

	/**
	 * 用于下拉框选择表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTables", method = { RequestMethod.POST })
	public List<String[]> getTables() {
		List<String[]> tableNames = EntityUtils.getTableNames();
		return tableNames;
	}

	/**
	 * 表头信息用于ajax
	 * 
	 * @param table
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{table}/columns", method = RequestMethod.POST)
	public List<TableTitle> openTable(@PathVariable String table) {
		List<TableTitle> titles = tablesService.getSimpleTitles(table);
		return titles;
	}

	/**
	 * 为弹出{@code datagrid}层赋必要的值。
	 * 
	 * @param model
	 *            存值的对象
	 * @param relatedtable
	 *            弹出层查询的表名
	 * @param relatedfield
	 *            弹出层表的关联列字段名
	 * @param table
	 *            父层表名
	 * @param field
	 *            父层的关联字段名。
	 * @param arrayLength
	 *            可填元素个数
	 * @return 转移到的页面
	 */
	@RequestMapping(value = "/{relatedtable}/popopen")
	public String openPopupTable(Model model, @PathVariable String relatedtable, @RequestParam(required = true) String relatedfield, @RequestParam(required = true) String table,
			@RequestParam(required = true) String field) {
		model.addAttribute("tablename", relatedtable);
		List<TableTitle> titles = tablesService.getSimpleTitles(relatedtable);
		model.addAttribute("columns", titles);
		model.addAttribute("relatedfield", relatedfield);
		model.addAttribute("field", field);
		String defaultLiteral = EntityUtils.getDefaultLiteral(table, field);
		model.addAttribute("defaultliteral", defaultLiteral);
		return "table_popup";
	}

	/**
	 * 根据id获得表数据
	 * 
	 * @param model
	 * @param table
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{table}/find", method = { RequestMethod.POST })
	public List<TableEntity> findTableListByIds(Model model, @PathVariable String table, @RequestParam(required = true) Long[] id) {
		Class<TableEntity> beanClass = EntityUtils.getMappedClass(table);
		List<TableEntity> data = tablesService.findTableListByIds(beanClass, id);
		return data;
	}

	/**
	 * 获得数组控制列的所有配置的值，及这列数据类型
	 * 
	 * @param tableName
	 * @param field
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/validateCombobox", method = RequestMethod.POST)
	public Map<String, Object> getKeyFiledValues(@RequestParam(required = true) String tableName, @RequestParam(required = true) String field) {
		Map<String, Object> model = Maps.newHashMap();
		List<String> valueSet = tablesService.getKeyOptions(tableName, field);
		model.put("options", valueSet);
		model.put("dataType", ReflectionUtils.getDeclaredField(EntityUtils.getMappedClass(tableName), field).getType().getSimpleName());
		return model;
	}

	/**
	 * 锁定表
	 * 
	 * @param table
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{table}/lockTable", method = { RequestMethod.POST })
	public String lockTable(@PathVariable String table, HttpServletRequest request) {
		String remoteAddr = request.getRemoteAddr();
		LockTableInterceptor.lockTable(table, remoteAddr);
		return "OK";
	}

	/**
	 * 解锁表
	 * 
	 * @param table
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{table}/unLockTable", method = { RequestMethod.POST })
	public String unLockTable(@PathVariable String table) {
		LockTableInterceptor.unLockTable(table);
		return "OK";
	}

	/**
	 * 下载表数据
	 * 
	 * @param table
	 * @return
	 */
	@RequestMapping(value = "/{table}/download", method = { RequestMethod.GET })
	public ResponseEntity<byte[]> downloadTable(@PathVariable String table) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", table + ".txt");

		Class<TableEntity> t = EntityUtils.getMappedClass(table);
		String downloadContent = tablesService.downloadContent(t);

		return new ResponseEntity<byte[]>(downloadContent.getBytes(Charset.forName("utf-8")), headers, HttpStatus.CREATED);
	}

	/**
	 * 下载文件夹
	 * 
	 * @param table
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@RequestMapping(value = "/{folder}/downloadFolder", method = { RequestMethod.GET })
	public void downloadFolder(@PathVariable String folder, HttpServletResponse response) throws IOException, ClassNotFoundException {
		String name = folder.substring(folder.lastIndexOf('.'));

		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=" + name + ".zip");
		response.setContentType("application/octet-stream; charset=utf-8");

		ZipOutputStream out = new ZipOutputStream(response.getOutputStream(), Charset.forName("utf-8"));
		List<Class<TableEntity>> classes = Lists.newArrayList();
		classes.addAll(ClassUtils.getSubClasses(folder, TableEntity.class));
		for (Class<TableEntity> t : classes) {
			if (!t.isInterface() && !t.getName().contains("conf")) {
				String downloadContent = tablesService.downloadContent(t);
				String path = t.getName().replaceAll(EntityUtils.PACKAGE + ".", "").replace(".", "/");
				out.putNextEntry(new ZipEntry(path + ".txt"));
				out.write(downloadContent.getBytes(Charset.forName("utf-8")));
			}
		}
		out.flush();
		out.close();
	}
	

	@ResponseBody
	@RequestMapping(value = "/upload", method = { RequestMethod.POST })
	public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
		Map<String, Object> map = Maps.newHashMap();
		try {
			if (file.isEmpty()) {
				map.put("msg", "文件无效！");
			} else {
				String tableName = file.getOriginalFilename().replaceAll(".txt", "");

				Class<TableEntity> mappedClass = EntityUtils.getMappedClass(tableName);

				List<TableEntity> resolve = FileToEntityUtils.resolve(mappedClass, file.getInputStream());

				List<TableArrayRule> tableArrayRules = tablesService.getTableArrayRule(tableName);
				for (TableEntity tableEntity : resolve) {

					for (TableArrayRule rule : tableArrayRules) {
						Object fieldValue = ReflectionUtils.getFieldValue(tableEntity, rule.getKeyField());
						String keyValue = rule.getKeyValue();
						if (String.valueOf(fieldValue).equals(keyValue)) {
							Object fieldValue2 = ReflectionUtils.getFieldValue(tableEntity, rule.getTargetField());
							int keyValueNum = rule.getKeyValueNum().intValue();
							int length = StringUtils.split(fieldValue2.toString(), ',').length;
							if (keyValueNum % length != 0) {
								map.put("msg", tableName + "中code为" + tableEntity.getCode() + "的" + rule.getTargetField() + "字段有问题,请检查");
								return map;
							} else {
								break;
							}
						}
					}

					tablesService.updateData(tableEntity);
				}
				map.put("successed", true);
				map.put("msg", file.getOriginalFilename() + "已上传到服务器");

			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "生成文件失败！" + e);
		}
		return map;
	}
}
