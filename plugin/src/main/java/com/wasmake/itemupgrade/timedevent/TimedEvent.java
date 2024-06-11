package com.wasmake.itemupgrade.timedevent;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TimedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    @Setter
    public UpdateTime updateTime;

    public TimedEvent(UpdateTime updateCause) {
        this.updateTime = updateCause;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}