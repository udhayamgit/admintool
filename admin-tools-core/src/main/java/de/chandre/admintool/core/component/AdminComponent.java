package de.chandre.admintool.core.component;

import java.util.List;
import java.util.Map;

/**
 * A admin component<br>
 * should have at least a minimum of one menu entry
 * @author Andre
 *
 */
public interface AdminComponent extends Comparable<AdminComponent>
{
	/**
	 * @return the displayName
	 */
	public String getDisplayName();

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName);
	/**
	 * @return the mainMenu
	 */
	public MenuEntry getMainMenu();
	/**
	 * @param mainMenu the mainMenu to set
	 */
	public void setMainMenu(MenuEntry mainMenu);
	/**
	 * @return the notificationTemplates
	 */
	public List<String> getNotificationTemplates();
	/**
	 * path to notification template shown in the top right menu<br>
	 * should start with a "li" tag. eg:<br>
	 * <code>
	 * 		&lt;li class="dropdown messages-menu"&gt;
	 * </code>
	 * @see https://almsaeedstudio.com/themes/AdminLTE/documentation/index.html#component-main-header
	 * 
	 * @param notificationTemplates the notificationTemplates to set
	 */
	public void setNotificationTemplates(List<String> notificationTemplates);
	/**
	 * path to notification template shown in the top right menu<br>
	 * should start with a "li" tag. eg:<br>
	 * <code>
	 * 		&lt;li class="dropdown messages-menu"&gt;
	 * </code>
	 * @see https://almsaeedstudio.com/themes/AdminLTE/documentation/index.html#component-main-header
	 * 
	 * @param notificationTemplate the template path to notification template
	 */
	public void addNotificationTemplate(String notificationTemplate);
	/**
	 * @return the additionalCSS
	 */
	public Map<String, Boolean> getAdditionalCSS();
	/**
	 *  map with paths to CSS.<br>
	 * 
	 * @param additionalCSS the additionalCSS to set
	 * @see #addAdditionalCSS(String, boolean)
	 */
	public void setAdditionalCSS(Map<String, Boolean> additionalCSS);
	/**
	 * path to CSS.<br>
	 * Example:<br>
	 *  <code>
	 *  addAdditionalCSS("/static/myComponent/css/myStyles.css", true);<br>
	 *  addAdditionalCSS("http://example.com/styles.css", false);
	 *  </code>
	 * @param additionalCSS the additionalCSS to set
	 */
	public void addAdditionalCSS(String additionalCSS, boolean relative);
	/**
	 * @return the additionalJS
	 */
	public Map<String, Boolean> getAdditionalJS();
	/**
	 * map with path to additional JavaScript files. <br>
	 * @param additionalJS the additionalJS to set
	 * @see #addAdditionalJS(String, boolean)
	 */
	public void setAdditionalJS(Map<String, Boolean> additionalJS);
	/**
	 * path to additional JavaScript files.<br>
	 * Example:<br>
	 *  <code>
	 *  addAdditionalJS("/static/myComponent/js/myScripts.js", true);<br>
	 *  addAdditionalJS("http://example.com/script.js", false);
	 *  </code>
	 * @param additionalJS the additionalJS to set
	 */
	public void addAdditionalJS(String additionalJS, boolean relative);
}
