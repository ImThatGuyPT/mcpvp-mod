package us.mcpvpmod.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import us.mcpvpmod.Server;
import us.mcpvpmod.config.ctf.ConfigCTF;
import us.mcpvpmod.config.kit.ConfigKit;
import us.mcpvpmod.config.maze.ConfigMaze;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

public class GuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft minecraftInstance) {

	}

	@Override
	public java.lang.Class<? extends GuiScreen> mainConfigGuiClass() {
		return MCPVPConfig.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(
			RuntimeOptionCategoryElement element) {
		return null;
	}
	
	public static class MCPVPConfig extends GuiConfig {
		
		public MCPVPConfig(GuiScreen parent) {
	        super(parent, getConfigElements(), "mcpvp", false, false, "MCPVP Configuration");
		}

		private static List<IConfigElement> getConfigElements() {
	        List<IConfigElement> list = new ArrayList<IConfigElement>();
	        
	        list.add(new DummyCategoryElement("All Servers", "mcpvp.config.mcpvp", AllConfig.class));
	        list.add(new DummyCategoryElement(Server.CTF.toString(), "mcpvp.config.CTF", ConfigCTF.class));
	        list.add(new DummyCategoryElement(Server.KIT.toString(), "mcpvp.config.Kit", ConfigKit.class));
	        list.add(new DummyCategoryElement(Server.MAZE.toString(), "mcpvp.config.Maze", ConfigMaze.class));
	        
	        return list;
		}
		

		
	}
}