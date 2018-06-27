package com.ngtesting.platform.contrl;

import com.ngtesting.platform.config.Constants;
import com.ngtesting.platform.service.intf.UserService;
import com.ngtesting.platform.utils.FileUtil;
import com.ngtesting.platform.vo.UserVo;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping(Constants.API_PATH_CLIENT + "/")
public class UploadContrl extends BaseContrl {
	@Autowired
	UserService userService;

	@ResponseBody
	@PostMapping("/upload")
	public Map<String, Object> uploadSingle(
            @RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> ret = new HashMap<String, Object>();

		UserVo userVo = (UserVo) request.getSession().getAttribute(Constants.HTTP_SESSION_USER_KEY);
		Long orgId = userVo.getDefaultOrgId();

		String origName = file.getOriginalFilename();
		String extName = FilenameUtils.getExtension(origName);
		String fileName = UUID.randomUUID().toString() + "." + extName;

		String uploadPath = FileUtil.SaveFile(file, "data/", fileName);

		ret.put("origName", origName);
		ret.put("uploadPath", uploadPath);

		float flt = Float.parseFloat(String.valueOf(file.getSize()));
		String fileSize = new DecimalFormat("##0.00").format(flt / 1000 / 1000);
		ret.put("fileSize", fileSize + 'M');

		ret.put("code", Constants.RespCode.SUCCESS.getCode());

		return ret;
	}

}
