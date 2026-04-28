package me.laysar.bastionhelper;

import me.contaria.speedrunapi.config.api.SpeedrunConfig;
import me.contaria.speedrunapi.config.api.annotations.Config;
import me.laysar.bastionhelper.client.handler.CreativeFollowHandler;
import me.laysar.bastionhelper.client.handler.HighlightPiglinsHandler;
import me.laysar.bastionhelper.client.handler.PiglinDeathHandler;
import me.laysar.bastionhelper.client.handler.ShowPiglinPathsHandler;

@Config(init = Config.InitPoint.PRELAUNCH)
public class BastionHelperConfig implements SpeedrunConfig {
	@Config.Category("visuals")
	public boolean showPathfinding = false;
	@Config.Category("visuals")
	public boolean showHighlights = true;

	@Config.Category("ui")
	public boolean showOverlay = true;
	@Config.Category("ui")
	public boolean deathMessages = false;

	@Config.Category("gameplay")
	public boolean creativeFollow = true;

	{
		BastionHelper.config = this;
	}

	@Override
	public String modID() {
		return BastionHelper.MOD_ID;
	}

	public void reapply() {
		ShowPiglinPathsHandler.showPaths = this.showPathfinding;
		HighlightPiglinsHandler.highlighted = this.showHighlights;
		CreativeFollowHandler.setFollow(this.creativeFollow);
		PiglinDeathHandler.setEnabled(this.deathMessages);
	}
}
