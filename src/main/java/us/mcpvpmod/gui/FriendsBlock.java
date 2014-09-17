package us.mcpvpmod.gui;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import us.mcpvpmod.config.mcpvp.ConfigFriends;
import us.mcpvpmod.config.mcpvp.ConfigHUD;
import us.mcpvpmod.game.vars.VarsCTF;

public class FriendsBlock {
	
	static ArrayList<String> display = new ArrayList<String>();

	static int padding = 3;
	static boolean bg = true;
	
	static String title = Format.process(ConfigFriends.onlineTitle);
	static String coords;
	static int baseX = ConfigHUD.margin;
	static int baseY = ConfigHUD.margin;
	static int w;
	static int h;
	
	boolean matchW = ConfigHUD.alignWidths;
	boolean matchH = ConfigHUD.alignHeights;
	
	static Minecraft mc = Minecraft.getMinecraft();
	static FontRenderer fontRenderer = mc.fontRenderer;
	
	/**
	 * The main call that displays the block.
	 * Updates information and dimensions then draws.
	 */
	public static void display() {
		fontRenderer = mc.fontRenderer;

		update();
		setW();
		setH();
		setX();
		setY();
		draw();
	}
	
	/**
	 * Replaces {variables} with their value.
	 * Handles formatting codes.
	 */
	public static void update() {
		display.clear();
		display = FriendsList.getFriends();
	}
	
	/**
	 * Calculates the width of the block based only on the current block.
	 * @return Width
	 */
	public static int calcW() {
		int calcW = 0;
		int lineW = 0;
		
		// Cycle through our information and calculate the length.
		for (String line : display) {		
			lineW = fontRenderer.getStringWidth(line);
			
			// Check if it's the longest yet.
			if (lineW > calcW) {
				calcW = lineW;
			}
		}
		
		// Include our title in the check.
		if (fontRenderer.getStringWidth(title) > calcW) {
			calcW = fontRenderer.getStringWidth(title);
		}
		return calcW;
	}
	
	/**
	 * Sets the width of the block.
	 * Accounts for matching widths.
	 */
	public static void setW() {
		w = calcW();
	}
	
	/**
	 * Calculates the height of only this block.
	 * @return Height
	 */
	public static int calcH() {
		int calcH;
		int stringHeight = display.size() * fontRenderer.FONT_HEIGHT;
		int spacing = (display.size()-1) * 2;
		calcH = 2 + stringHeight + spacing + 10;
		
		return calcH;
	}
	
	/**
	 * Sets the height of the block.
	 */
	public static void setH() {
		h = calcH();
	}
	
	/**
	 * Responsible for setting the X coordinate of the block.
	 */
	public static void setX() {
		ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int shiftX = (res.getScaledWidth()/100) * 1;
		baseX = res.getScaledWidth() - (int) shiftX - w - padding;
	}
	
	/**
	 * Responsible for setting the Y coordinate of the block.
	 */
	public static void setY() {
		ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int shiftY = (res.getScaledWidth()/100) * 1;
		baseY = (int) (shiftY) + padding;
	}
	
	/**
	 * Draw the block!
	 */
	public static void draw() {
		int titleHeight = fontRenderer.FONT_HEIGHT-1;
		bg = ConfigHUD.renderBG;
		
		if (bg) {
			if (title != "") {
				
				// Render the title area.
				Draw.rect(baseX-padding, 
							   baseY-1-padding, 
							   w+padding*2,
							   titleHeight+padding*2, 
							   0, 0, 0, (float) 0.42/2);
				
				// Render the bottom half.
				Draw.rect((float) baseX-padding,
						   (float) baseY+titleHeight+2, 
						   (float) w+padding*2,
						   (float) h + padding - titleHeight - 3, 
						   0, 0, 0, (float) 0.32/2);
				
			} else {
				// Render the bottom half, without a title.
				Draw.rect((float) baseX-padding,
						   (float) baseY+2, 
						   (float) w+padding*2,
						   (float) h + padding, 
						   0, 0, 0, (float) 0.32/2);

			}
		}
		int offset = padding*2;
		
		// Render the title.
		if (title != "") {
			// Check our config for centering titles.
			if (ConfigHUD.centerTitles) {
				fontRenderer.drawStringWithShadow(title, baseX + centerPos(w, title), baseY-1, 0xFFFFFF);
			} else {
				fontRenderer.drawStringWithShadow(title, baseX, baseY-1, 0xFFFFFF);
			}
			// Set our offset. All titles result in an offset of 12.
			offset = 12;
		}

		// Render all other strings under the title.
		for (String string : display) {
			fontRenderer.drawStringWithShadow(string, baseX, baseY+offset, 0xFFFFFF);
			offset = offset + fontRenderer.FONT_HEIGHT+2;
		}
	}
	
	/**
	 * Processes {variables} in text, e.g. {kills}
	 * @param line
	 * @return
	 */
	public static String processVars(String line) {
		String reVar = "\\{(.+?)\\}";

		// Form our matcher for variables.
		Matcher varMatch = Pattern.compile(reVar).matcher(line);
		while (varMatch.find()) {
			String var = varMatch.group().replaceAll("\\{", "").replaceAll("\\}", "");

			if (VarsCTF.vars.get(var) != null) {		
				// Replace the occurance of the var with the actual info.
				line = line.replaceAll("\\{" + var + "\\}", VarsCTF.vars.get(var));
			} else {
				// Return "null" to prevent the string from being rendered.
				line = "null";
			}
		}
		return line;
	}

	/**
	 * Calculate the center position of text.
	 * @param area
	 * @param string
	 * @return
	 */
	public static int centerPos(int area, String string) {
		return ((area/2) - (fontRenderer.getStringWidth(string)/2));
	}
	
	/**
	 * Sets whether or not to match width.
	 * @param match
	 * @return
	 */
	public FriendsBlock setMatch(boolean match) {
		matchW = match;
		return this;
	}
	
}