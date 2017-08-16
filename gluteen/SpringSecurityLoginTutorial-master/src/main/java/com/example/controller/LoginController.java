package com.example.controller;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import com.example.model.Post;
import com.example.model.Title;
import com.example.service.PostService;
import com.example.service.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.model.User;
import com.example.service.UserService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private TitleService titleService;

	@Autowired
	private PostService postService;

	@RequestMapping(value={"/login"}, method = RequestMethod.GET)
	public ModelAndView login(){
		ModelAndView modelAndView = new ModelAndView();

		List<Title> titles = titleService.getTitlesOrderByDate();
		modelAndView.addObject("titles",titles);
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@RequestMapping(value="/registration", method = RequestMethod.GET)
	public ModelAndView registration(){
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		List<Title> titles = titleService.getTitlesOrderByDate();
		modelAndView.addObject("titles",titles);
		modelAndView.addObject("user", user);
		modelAndView.setViewName("registration");
		return modelAndView;
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult
					.rejectValue("email", "error.user",
							"Bu email kayıtlı, lütfen başka bir email adresi deneyin..");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "kaydınız gerçekleştirildi, giriş yapabilirsiniz..");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("registration");
			
		}
		return modelAndView;
	}
	// if authentication
	//****************************************************************************//
	//	modelAndView.addObject("post",new Post());
	@RequestMapping(value = "/posts/{id}", method = RequestMethod.POST)
	public String savePost(@Valid @ModelAttribute("posts")  Post post,@PathVariable("id")Long id){

		ModelAndView modelAndView =new ModelAndView();

		List<Title> titles = titleService.getTitlesOrderByDate();
		modelAndView.addObject("titles",titles);

		Title title = titleService.getTitleById(id);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());

		Date date = new Date();

		Post post1 = new Post();

		post1.setPostBody(post.getPostBody());
		post1.setPostDate(date);
		post1.setPostSender(user);
		post1.setPostTitle(title);

		postService.savePost(post1);
		//modelAndView.addObject("post",post1);
		//modelAndView.setViewName("homePage2Login");
		//modelAndView.addObject("post",new Post());
		return "redirect:/posts/"+title.getId();
	}

	@RequestMapping(value = "/posts/{id}")
	public ModelAndView postGetLogin(ModelAndView modelAndView,@PathVariable("id")Long id){

		List<Title> titles = titleService.getTitlesOrderByDate();

		List<Post> posts = postService.findByPostTitleOrderByPostDate(id);

		Title titleId = titleService.getTitleById(id);

		modelAndView.addObject("titles",titles);
		modelAndView.addObject("titleId",titleId.getId());
		modelAndView.addObject("posts",posts);
		modelAndView.addObject("post",new Post());

		modelAndView.setViewName("homePage2Login");

		return modelAndView;
	}

	@RequestMapping(value = "/titles")
	public ModelAndView postTitle(){
		ModelAndView modelAndView =new ModelAndView();

		List<Title> titles = titleService.getTitlesOrderByDate();
		modelAndView.addObject("titles",titles);
		modelAndView.addObject("title",new Title());

		modelAndView.setViewName("homePageLogin");

		return modelAndView;
	}
	@RequestMapping(value = "/titles", method = RequestMethod.POST)
	public String saveTitle(@Valid @ModelAttribute("title")Title title){

		ModelAndView modelAndView =new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());

		Date date = new Date();

		Title title1 = new Title();

		title1.setTitleBody(title.getTitleBody());
		title1.setTitleCreated(date);
		title1.setTitleCreater(user);

		titleService.createTitle(title1);
		//modelAndView.addObject("post",post1);
//		modelAndView.setViewName("homePageLogin");
		//modelAndView.addObject("post",new Post());
		return "redirect:/posts/"+title1.getId();
	}
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deletePost(@Valid @ModelAttribute("post")  Post post,@PathVariable("id")Long id){

		ModelAndView modelAndView =new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		List<Title> titles = titleService.getTitlesByPerson(user.getId());
		List<Post> posts = postService.getPostsByPerson(user.getId());

		if (user == null)
		{
			modelAndView.setViewName("login");
			return "redirect:/login";
		}
		postService.deletePost(id);

		modelAndView.addObject("titles",titles);
		modelAndView.addObject("posts",posts);
		modelAndView.addObject("user",user);
		//modelAndView.addObject("post",post1);
		modelAndView.setViewName("profile");
		//modelAndView.addObject("post",new Post());
		return "redirect:/profile";
	}
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView profile(){
		ModelAndView modelAndView =new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = userService.findUserByEmail(auth.getName());
		List<Title> titles = titleService.getTitlesByPerson(user.getId());

		List<Post> posts = postService.getPostsByPerson(user.getId());

		modelAndView.addObject("titles",titles);
		modelAndView.addObject("posts",posts);
		modelAndView.addObject("user",user);
		modelAndView.addObject("userName",user.getName());


		modelAndView.setViewName("profile");
		return modelAndView;
	}

	@RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
	public ModelAndView editPost(ModelAndView modelAndView,@PathVariable("id")Long id){

		List<Title> titles = titleService.getTitlesOrderByDate();

		Post posts = postService.getPostById(id);

		//Title titleId = titleService.getTitleById(id);

		modelAndView.addObject("titles",titles);
		modelAndView.addObject("titleId",posts.getId());
		modelAndView.addObject("posts",posts);
		modelAndView.addObject("post",new Post());

		modelAndView.setViewName("homePage2Login");

		return modelAndView;
	}
	@RequestMapping(value = "/profile/{id}", method = RequestMethod.POST)
	public String editPost(@Valid @ModelAttribute("posts")  Post post,@PathVariable("id")Long id){

		ModelAndView modelAndView =new ModelAndView();

		List<Title> titles = titleService.getTitlesOrderByDate();
		modelAndView.addObject("titles",titles);

		Post postt = postService.getPostById(id);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());

		Post post1 = new Post();
		post1.setId(postt.getId());
		post1.setPostBody(post.getPostBody());
		post1.setPostDate(postt.getPostDate());
		post1.setPostSender(user);
		post1.setPostTitle(postt.getPostTitle());

		postService.savePost(post1);
		//modelAndView.addObject("post",post1);
		//modelAndView.setViewName("homePage2Login");
		//modelAndView.addObject("post",new Post());
		return "redirect:/posts/"+postt.getPostTitle().getId();
	}
	// if authentication
	//****************************************************************************//
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView homePage(){
		ModelAndView modelAndView =new ModelAndView();
		List<Title> titles = titleService.getTitlesOrderByDate();
		modelAndView.addObject("titles",titles);
		modelAndView.setViewName("index");

		return modelAndView;
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView homePage(ModelAndView modelAndView,@PathVariable("id")Long id){

		List<Title> titles = titleService.getTitlesOrderByDate();

		List<Post> posts = postService.findByPostTitleOrderByPostDate(id);

		Title titleId = titleService.getTitleById(id);
		modelAndView.addObject("post",new Post());
		modelAndView.addObject("titles",titles);
		modelAndView.addObject("titleId",titleId);
		modelAndView.addObject("posts",posts);

		modelAndView.setViewName("homePage2");

		return modelAndView;
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public ModelAndView savePostt(@Valid @ModelAttribute("post")  Post post,@PathVariable("id")Long id){

		ModelAndView modelAndView =new ModelAndView();

		Title title = titleService.getTitleById(id);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());

		Date date = new Date();

		Post post1 = new Post();

		post1.setPostBody(post.getPostBody());
		post1.setPostDate(date);
		post1.setPostSender(user);
		post1.setPostTitle(title);

		postService.savePost(post1);
		//modelAndView.addObject("post",post1);
		modelAndView.setViewName("homePage2");
		//modelAndView.addObject("post",new Post());
		return modelAndView;
	}
	@RequestMapping(value = "/ara/{aranan}",method = RequestMethod.POST)
	public ModelAndView titleAra( @PathVariable("aranan") String aranan){
		ModelAndView modelAndView =new ModelAndView();
		List<Title> titles = titleService.titleAra(aranan);
		modelAndView.addObject("titles",titles);
		modelAndView.setViewName("arananTemplate");
		return modelAndView;
	}
	@RequestMapping(value = "/ara",method = RequestMethod.GET)
	public ModelAndView titleAraa(Model model){
		ModelAndView modelAndView =new ModelAndView();

		modelAndView.setViewName("arananTemplate");
		return modelAndView;
	}

	@RequestMapping(value="/admin/home", method = RequestMethod.GET)
	public ModelAndView home(){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
		modelAndView.setViewName("admin/home");
		return modelAndView;
	}

	public ModelAndView titles(ModelAndView modelAndView){


		modelAndView.setViewName("titleFragment");

		return modelAndView;
	}

	@RequestMapping("/nedir")
	public ModelAndView nedir(){
		ModelAndView modelAndView =new ModelAndView();
		List<Title> titles = titleService.getTitlesOrderByDate();
		modelAndView.addObject("titles",titles);
		modelAndView.setViewName("nedir");

		return modelAndView;

	}
	@RequestMapping("/kullanim")
	public ModelAndView kullanim(){
		ModelAndView modelAndView =new ModelAndView();
		List<Title> titles = titleService.getTitlesOrderByDate();
		modelAndView.addObject("titles",titles);
		modelAndView.setViewName("kullanim");

		return modelAndView;

	}




}
