package play.teslafuck.client.mods.player;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import play.teslafuck.client.main.Category;
import play.teslafuck.client.main.TFClient;
import play.teslafuck.client.mods.Mod;
import play.teslafuck.client.utils.BlockUtils;

public class NoFall extends Mod {

    private boolean aac5doFlag = false;
    private boolean aac5Check = false;
    private int aac5Timer = 0;
    
    private boolean isFlagged = false;
	
	public NoFall() {
		super("NoFall", "NoFall", Keyboard.KEY_T, Category.PLAYER);
	}
	
	public void onEnabled() {
		super.onEnabled();

        aac5Check = false;
        aac5doFlag = false;
        isFlagged = false;
        
        aac5Timer = 0;
	}
	
	@Override
	public void onUpdate() {
        double offsetYs = 0.0;
        aac5Check = false;

        while (mc.player.motionY - 1.5 < offsetYs) {
            BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY + offsetYs, mc.player.posZ);
            Block block = BlockUtils.getBlock(blockPos);
            AxisAlignedBB axisAlignedBB = block.getCollisionBoundingBox(BlockUtils.getState(blockPos), mc.world, blockPos);
            if (axisAlignedBB != null) {
                offsetYs = -999.9;
                aac5Check = true;
            }
            offsetYs -= 0.5;
        }
        if (mc.player.onGround) {
            mc.player.fallDistance = -2f;
            aac5Check = false;
        }
        if (aac5Timer > 0) {
            aac5Timer -= 1;
        }
        if (aac5Check && mc.player.fallDistance > 2.5 && !mc.player.onGround) {
            aac5doFlag = true;
            aac5Timer = 18;
        } else {
            if (aac5Timer < 2) {
            	aac5doFlag = false;
            	isFlagged = false;
            }
        }
        if (aac5doFlag && !isFlagged) {
            mc.player.moveForward = 0f;
            mc.player.moveStrafing = 0f;
            if (mc.player.onGround) {
                mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1, mc.player.posZ, false));
                isFlagged = true;
            } else {
            	mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1, mc.player.posZ, false));
            }
        }
        
	}
}
