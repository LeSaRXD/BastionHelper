package me.laysar.bastionhelper;

import me.contaria.speedrunapi.config.api.SpeedrunConfig;
import me.contaria.speedrunapi.config.api.annotations.Config;
import me.laysar.bastionhelper.client.handler.ShowPiglinPathsHandler;

@Config(init = Config.InitPoint.PRELAUNCH)
public class BastionHelperConfig implements SpeedrunConfig {
	@Config.Category("toggles")
	public boolean showPathfinding = false;

	{
		BastionHelper.config = this;
	}

	@Override
	public String modID() {
		return BastionHelper.MOD_ID;
	}

	public void reapply() {
		ShowPiglinPathsHandler.showPaths = this.showPathfinding;
	}
}
