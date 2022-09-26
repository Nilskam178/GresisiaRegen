package gresisiaregen.gresisiaregen;

import org.bukkit.Material;

public enum CropTypes {
    WHEAT(Material.WHEAT),
    POTATO(Material.POTATO),
    CARROT(Material.CARROT),
    BEETROOT(Material.BEETROOTS);

    private Material material;

    public Material getMaterial() {
        return material;
    }

    CropTypes(Material material) {
        this.material = material;
    }
}
