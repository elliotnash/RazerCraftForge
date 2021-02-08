package org.elliotnash.razercraft;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elliotnash.razer.RazerController;

public class RazerCraftClient {
    private static final Logger LOGGER = LogManager.getLogger();
    static RazerController controller;
    Minecraft minecraftClient;
    public RazerCraftClient() {

        LOGGER.info("And registered!");

        //Setup controller and draw default
        controller = new RazerController();
        controller.draw();

        minecraftClient = Minecraft.getInstance();

        MinecraftForge.EVENT_BUS.register(this);
    }


    private void setActiveKey(){
        controller.activeSlot = lastSlot;
        controller.draw();
    }

    private void setFilledSlots(){
        controller.filledSlots.clear();
        for (int i = 0; i < 9; i++){
            if (!minecraftClient.player.inventory.getStackInSlot(i).isEmpty()){
                //stack isn't empty, add it to filled slots
                controller.filledSlots.add(i);
            }
        }
    }

    Integer lastSlot = null;
    @SubscribeEvent
    public void onInvEvent(TickEvent.PlayerTickEvent event){
        int currentSlot = minecraftClient.player.inventory.currentItem;
        if (lastSlot == null || lastSlot != currentSlot){
            lastSlot=currentSlot;
            setFilledSlots();
            setActiveKey();
        }
    }

    @SubscribeEvent
    public void onLogOut(ClientPlayerNetworkEvent.LoggedOutEvent event){
        System.out.println("Logged out, clearing active key");
        //set filled slots and last slot to null
        controller.filledSlots.clear();
        lastSlot = null;
        setActiveKey();
    }

}
