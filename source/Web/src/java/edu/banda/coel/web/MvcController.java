package edu.banda.coel.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MvcController {

    @RequestMapping(value = "/test.dispatch", method=RequestMethod.GET)
    public ModelAndView getText() {
    	System.out.println("test.dispatch reached");
        return new ModelAndView("test", new ModelMap("text", "from MVC controller"));
    }
}
