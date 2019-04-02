package com.atguigu.springmvc.handlers;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.atguigu.springmvc.entities.User;

//@SessionAttributes(value={"user"},types={String.class}) //可以让user即放在请求域里面，又放在session域里面
@RequestMapping("springmvc")
@Controller
public class SpringMVCTest {

    private static final String SUCCESS = "success";
    
    @RequestMapping("/testRedirect")
    public String testRedirect(){
    	System.out.println("testRedirect");
    	return "redirect:/index.jsp";
    }
    
    @RequestMapping("/testViewAndViewResolver")
    public String testViewAndViewResolver(){
    	System.out.println("testViewAndViewResolver");
    	return SUCCESS;
    }
    
    /*
     * 有@ModelAndView标记的方法，会在每个目标方法执行之前被SpringMVC调用！ 
     */
    @ModelAttribute
    public void getUser(@RequestParam(value="id",required=false)Integer id,
    		Map<String,Object> map){
    	System.out.println("modelAttribute method");
    	if(id!=null){
    		//模拟从数据库获取信息
    		User user = new User(1,"Tom","123456","tom@atguigu.com",12);
    		System.out.println("从数据库中获取一个对象" + user);
    		map.put("user", user);
    	}
    }
    
    /*
     * 运行原理：
     * 1，执行@ModelAttribute注解修饰的方法：从数据库取出对象，把对象放入到了Map中。键为：user
     * 2，SpringMVC从Map中取出User对象，并把表单的请求参数赋给该User对象的对应属性
     * 3，SpringMVC把上述对象传入目标方法的参数
     * 
     * 注意：在@ModelAndView修饰的方法中，放入到Map时的键需要和目标方法入参类型的第一个小写字母小写的字符串一致
     */
    @RequestMapping("/testModelAttribute")
    public String testModelAttribute(User user){
    	System.out.println("修改：" + user);
    	return SUCCESS;
    }
    
    /*
     * @SessionAttributes除了可以通过属性指定需要放到会话中的属性外（实际上使用的是value属性值）
     * 还可以通过模型属性的对象类型指定那些模型属性需要放到会话中（实际上使用的是types属性值）
     * 
     * 注意@SessionAttributes只能放在类的上面，而不能放在方法的上面
     */
    @RequestMapping("/testSessionAttributes")
    public String testSessionAttributes(Map<String,Object> map){
    	User user = new User("Tom","123456","TOM@atguigu.com",15);
    	map.put("user", user);
    	map.put("school","atguigu");
    	return SUCCESS;
    }
    
    /*
     * 目标方法可以添加Map类型(实际上也可以是Model类型或ModelMap类型)的参数
     */
    @RequestMapping("/testMap")
    public String testMap(Map<String,Object> map){
    	System.out.println(map.getClass().getName());
        map.put("names", Arrays.asList("Tom","Jerry","Mike"));
    	return SUCCESS;
    }
    
    /*
     * 目标方法的返回值可以是ModelAndView
     * 其中可以包含视图和模型信息
     * SpringMVC会把ModelAndView的model中数据放入到request域对象中
     */
    @RequestMapping("/testModelAndView")
    public ModelAndView testModelAndView(){
    	String viewName = SUCCESS;
    	ModelAndView modelAndView = new ModelAndView(viewName);
    	
    	//添加模型数据到ModelAndView中，
    	modelAndView.addObject("time",new Date());
    	return modelAndView;
    }
    
    /*
     * 可以使用Servlet原生的API作为目标方法的参数 具体支持一下类型
     * HttpServeltRequest
     * HttpServletResponse
     * HttpSession
     * java.security.Principal
     * OutputStream
     * Reader
     * Writer
     */
    @RequestMapping("/testServletAPI")
    public void testServletAPI(HttpServletRequest request,
    		HttpServletResponse response,Writer out) throws IOException{
    	System.out.println("testServletAPI,"+ request+","+response);
    	out.write("hello springmvc");
    	//return SUCCESS;
    }
    
    @RequestMapping("/testPojo")
    public String testPojo(User user){
    	System.out.println("testPojo："+user);
    	return SUCCESS;
    }
    
    @RequestMapping("/testCookieValue")
    public String testCookieValue(@CookieValue("JSESSIONID")String sessionId){
    	System.out.println("testCookieValue:sessionId:" + sessionId);
    	return SUCCESS;
    }
    
    /*
     * @RequestParam来映射请求参数
     * value值即请求参数的参数名
     * required该参数是否必须 ，默认为true
     * defaultValue请求参数的默认值
     */
    @RequestMapping(value="/testRequestParam")
    public String testRequestParam(@RequestParam(value="username") String un,
    		 @RequestParam(value="age", required=false,defaultValue="0") int age){
    	System.out.println("testRequestParam,username:"+un+",age:"+age);
    	return SUCCESS;
    }
    
    @RequestMapping(value="/testRest/{id}",method=RequestMethod.PUT)
    public String testRestPut(@PathVariable Integer id){
    	System.out.println("testRest PUT:"+id);
    	return SUCCESS;
    }
    
    @RequestMapping(value="/testRest/{id}",method=RequestMethod.DELETE)
    public String testRestDelete(@PathVariable Integer id){
    	System.out.println("testRest DELETE:"+id);
    	return SUCCESS;
    }
    
    @RequestMapping(value="/testRest",method=RequestMethod.POST)
    public String testRest(){
    	System.out.println("testRest POST");
    	return SUCCESS;
    }
    
    @RequestMapping(value="/testRest/{id}",method=RequestMethod.GET)
    public String testRest(@PathVariable Integer id){
    	System.out.println("testRest GET:"+id);
    	return SUCCESS;
    }
    
    /*
     * @PathVariable可以用来映射URL中的占位符到目标方法的参数中
     */
    @RequestMapping("/testPathVariable/{id}")
    public String testPathVariable(@PathVariable("id") Integer id){
    	System.out.println("testPathVariable:" + id);
    	return SUCCESS;
    }
    
    @RequestMapping("/testAntPath/*/abc")
    public String testAntPath(){
    	System.out.println("testAntPath");
    	return SUCCESS;
    }
    /*
     * 了解：可以使用params和headers来更加精确的映射请求，params和headers支持简单的表达式
     */
    @RequestMapping(value="testParamsAndHeaders",
    		params={"username","age!=10"},headers={"Accept-Language=zh-CN,zh;1=0.8"})
    public String testParamsAndHeaders(){
    	System.out.println("testParamsAndHeaders");
    	return SUCCESS;
    }
    
    
    /*
     * 使用method属性，指定请求方式
     */
    @RequestMapping(value="/testMethod",method=RequestMethod.POST)
    public String testMethod(){
    	System.out.println("testMethod");
    	return SUCCESS;
    }
	/*
	 * 1.@RequestMapping 除了修饰方法，还可以修饰类
	 * 2，
	 * 1），类定义处：提供初步的请求映射信息，相对于web应用的根目录
	 * 2），方法出处：提供进一步的细节映射信息，相对于类定义出的URL，若类定义处未标注@RequestMapping,则方法处标记的URL相对于WEB应用的根目录
	 */
    @RequestMapping("testRequestMapping")
	public String testRequestMapping(){
		System.out.println("testRequestMapping");
		return SUCCESS;
	}
}
