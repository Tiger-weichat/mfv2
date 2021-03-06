package webcat.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import webcat.utils.ConEnum;
import webcat.utils.Constants;
import webcat.utils.HttpClientUtils;

@Controller
@RequestMapping(value = "/author")
public class AuthorizationController extends AbstractController{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value = "/author")
	public ModelAndView author()throws Exception {
		
		String code = request.getParameter("code");
		String redirect_url = request.getParameter("redirect_url");
		String openid = getOAuthOpenId(Constants.appID, Constants.appsecret, code);
		if(StringUtils.isNotBlank(openid)){
			session.setAttribute("open_id", openid);
		}

		return new ModelAndView("redirect:" + ConEnum.MenuUrl.getValue(redirect_url));
	}
	
	/**
	 * 通过授权接口获取用户的openid
	 * @param appid
	 * @param secret
	 * @param code
	 * @return
	 */
	public static String getOAuthOpenId(String appid, String secret, String code ) {
		String openid = null;
		if(StringUtils.isNotBlank(code)){
			String o_auth_openid_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	        String requestUrl = o_auth_openid_url.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code);

	        HttpClientUtils hc = new HttpClientUtils();
	        
	        String mes = hc.get(requestUrl);

	        JSONObject jsonObject =  JSONObject.parseObject(mes);
	        
	        if(jsonObject != null){
	        	openid = jsonObject.getString("openid");
	        }
		}
        return openid;
    }
	
	
}
