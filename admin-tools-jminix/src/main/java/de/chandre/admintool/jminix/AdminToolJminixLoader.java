package de.chandre.admintool.jminix;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.chandre.admintool.core.AbstractAdminToolLoader;
import de.chandre.admintool.core.AdminTool;
import de.chandre.admintool.core.component.AdminComponent;
import de.chandre.admintool.core.component.AdminComponentImpl;
import de.chandre.admintool.core.component.MenuEntry;

/**
 * @author Andre
 */
@Component
public class AdminToolJminixLoader extends AbstractAdminToolLoader
{
	private static final Log LOGGER = LogFactory.getLog(AdminToolJminixLoader.class);
	
	@Autowired
	private AdminTool adminTool;
	
	@Autowired
	private AdminToolJminixConfig config;
	
	@PostConstruct
	public void configureAdminTool()
	{
		if(!coreConfig.isEnabled() || !config.isEnabled()) {
			LOGGER.info("admin tool's jminix browser integation is deactivated");
			return;
		}
		
		LOGGER.info("adding JMX Console to admin tool");
		LOGGER.debug(toString());
		boolean relative = !shouldCDNsUsed();
		
		AdminComponent component = new AdminComponentImpl();
		component.setPosition(config.getComponentPosition());
		component.getSecurityRoles().addAll(config.getSecurityRoles());
		component.setDisplayName("JMX Console");
		
//		component.addAdditionalJS("/webjars/jquery-ui/1.12.1/jquery-ui.min.js", true);
//		component.addAdditionalJS("/webjars/jquery-ui/1.12.1/ui/widgets/resizable.js", true);
		component.addAdditionalJS(getWebjarsPrefixUri() + "mustache/" + config.getMustacheVersion() + "/mustache.min.js", relative);
		component.addAdditionalJS("/webjars/jstree/3.3.4/dist/jstree.js", true);
		
		component.addAdditionalJS("/static/admintool/jmx/js/jquery-resizable.js", true);
		component.addAdditionalJS("/static/admintool/jmx/js/jmx.js", true);
		
		component.addAdditionalCSS("/webjars/jstree/3.3.4/dist/themes/default/style.css", true);
		component.addAdditionalCSS("/static/admintool/jmx/css/jmx.css", true);
		
		
		MenuEntry mainMenu = new MenuEntry();
		mainMenu.setDisplayName("JMX");
		mainMenu.setName("jmx");
		mainMenu.setTarget("content/jmx/jmx");
		mainMenu.setResouceMessageKey(AdminTool.RESOURCE_MESSAGE_KEY_PREFIX + "jmx.displayName");
		component.setMainMenu(mainMenu);
		
		adminTool.addComponent(component);
	}
}
