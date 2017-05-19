package io.hellsing.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import io.hellsing.data.MemoDAO;
import io.hellsing.data.PersistenceDAO;
import io.hellsing.data.User;
import io.hellsing.data.ValidateDAO;

@Controller
@SessionAttributes("user")
public class MemoController {

//	@ModelAttribute("")
	@Autowired
	WebApplicationContext wac;
	
	@Autowired
	MemoDAO mdao;
	
	@Autowired
	ValidateDAO vdao;
	
	@Autowired
	PersistenceDAO pdao;
	
	@RequestMapping("start.do")
	public ModelAndView landingPage(){
		ModelAndView mv = new ModelAndView();
		
		
		mv.setViewName("landing.jsp");
		return mv;
	}
	@RequestMapping("login.do")
	public ModelAndView loginPage(){
		ModelAndView mv = new ModelAndView();
		
		
		mv.setViewName("login.jsp");
		return mv;
	}
	@RequestMapping("register.do")
	public ModelAndView accountRegistration(){
		ModelAndView mv = new ModelAndView();
		User user = new User();
		
		mv.addObject("user", user);
		mv.setViewName("accountCreation.jsp");
		return mv;
	}
	
	
	@RequestMapping("validate.do")
	public ModelAndView landingPage(@Valid User user, Errors Errors){
		ModelAndView mv = new ModelAndView();
		System.out.println(user);
		if(vdao.accountExists(user)){
			System.out.println("It worked");
			System.out.println("This user already exists");
			mv.setViewName("accountCreation.jsp");
			return mv;
		}
		
		pdao.writeToFile(user, wac);
		mv.setViewName("accountMemos.jsp");
		
		return mv;
	}
	@RequestMapping(path="loginAttempt.do", method=RequestMethod.POST)
	public ModelAndView loginPage(@Valid User user, Errors Errors, String email, String password){
		ModelAndView mv = new ModelAndView();
		System.out.println(user);
		user.setEmail(email);
		user.setPassword(password);
		if(vdao.accountExists(user)){
			System.out.println("It worked 1");
			if(vdao.passwordMatches(user)){
				System.out.println("It worked 2");
				mv.setViewName("accountMemos.jsp");
				return mv;
			}
		}
		mv.setViewName("login.jsp");
		return mv;
	}
	
	
}
