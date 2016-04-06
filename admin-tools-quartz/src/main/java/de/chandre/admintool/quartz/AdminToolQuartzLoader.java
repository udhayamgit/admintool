package de.chandre.admintool.quartz;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.chandre.admintool.core.AdminTool;
import de.chandre.admintool.core.component.AdminComponent;
import de.chandre.admintool.core.component.AdminComponentImpl;
import de.chandre.admintool.core.component.MenuEntry;

/**
 * @author Andre
 */
@Component
public class AdminToolQuartzLoader
{
	private static final Log LOGGER = LogFactory.getLog(AdminToolQuartzLoader.class);
	
	@Autowired
	private AdminTool adminTool;
	
	@PostConstruct
	public AdminTool configureAdminTool()
	{
		LOGGER.info("adding Quartz view to admin tool");
		
		AdminComponent component = new AdminComponentImpl();
		component.setDisplayName("Quartz");
		component.addAdditionalCSS("/static/admintool/quartz/css/quartz.css", true);
		component.addAdditionalJS("/static/admintool/quartz/js/quartz.js", true);
		
		component.addAdditionalJS("/static/admintool/quartz/js/validator.min.js", true);
		
		MenuEntry mainMenu = new MenuEntry();
		mainMenu.setDisplayName("Quartz");
		mainMenu.setName("quartz");
		mainMenu.setTarget("content/quartz");
		component.setMainMenu(mainMenu);
		
		mainMenu.addSubmenuEntry(new MenuEntry("quartzconfig", "Quartz-Config", "quartz/content/quartzConfig"));
		mainMenu.addSubmenuEntry(new MenuEntry("quartzjobs", "Quartz-Jobs", "quartz/content/quartzJobs"));
		adminTool.addComponent(component);
		return adminTool;
	}
}