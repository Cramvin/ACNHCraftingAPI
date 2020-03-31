import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Recipe {

	private String name;
	private File file;
	private String[] material;
	private int[] materialCount; 
	
	public Recipe(String itemToCraft, String[] craftingMaterial, int[] materialCount) { // creates new recipe holder
		this.file = new File("recipes/recipes/" + itemToCraft + ".recipe");
		this.name = itemToCraft;
		this.material = craftingMaterial;
		this.materialCount = materialCount;
	}
	
	public void save(){ //saves recipe if needed
		try {
			if(!file.exists())
				file.createNewFile();
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for(int i = 0; i < material.length; i++) {
				for(int j = 0; j < materialCount[i]; j++) {
					bw.write(material[i]);
					bw.newLine();
				}
			}
			bw.close();
		} catch (IOException e) {
			System.out.println("Could not save recipe " + name + " properly: " + e.toString());
		}
	}
	
	public String getName() { // gets item name for what is crafted
		return name;
	}
	
	public void setName(String input) { // sets item name for what is crafted
		this.name = input;
	}
	
	public String[] getCraftingMaterials() { // gets materials for what is crafted
		return material;
	}
	
	public void setCraftingMaterials(String[] input) { // sets materials for what is crafted
		this.material = input;
	}
	
	public boolean isCraftingMaterial(String materialName) { // checks if materialName is material for recipe
		for(int i = 0; i < material.length; i++)
			if(material[i].equals(materialName)) {
				return true;
			}
		return false;
	}
	
	public int[] getCraftingMaterialCounts() { // gets count for materials for what is crafted
		return materialCount;
	}
	
	public void setCraftingMaterialCounts(int[] input) { // sets count for materials for what is crafted
		this.materialCount = input;
	}
	
	public int getCraftingMaterialCount(String materialName) { // gets count for material for recipe
		int id = -1;
		for(int i = 0; i < material.length; i++)
			if(material[i].equals(materialName)) {
				id = i; break;
			}
		if(id < 0)
			return 0;
		else
			return materialCount[id];
	}
	
	public void setCraftingMaterialCount(String materialName, int input) { // sets count for material for recipe
		int id = -1;
		for(int i = 0; i < material.length; i++)
			if(material[i].equals(materialName)) {
				id = i; break;
			}
		if(id < 0)
			System.out.println("There is no item \"" + materialName + "\" in recipe \"" + name + "\"!");
		else
			this.materialCount[id] = input;
	}
	
}
