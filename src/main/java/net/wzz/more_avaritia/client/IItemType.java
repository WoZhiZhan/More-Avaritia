package net.wzz.more_avaritia.client;

public interface IItemType {
    default Type getItemType() {
        return Type.ITEM;
    }

    enum Type {
        ITEM,
        TOOL,
        BOW,
        BLOCK
    }
}
