import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Recipe {

	private String name;
	private File file;
	private String[] material;
	private int[] materialCount; 
	
	public Recipe(String itemToCraft, String[] craftingMaterial, int[] materialCount) {
		this.file = new File("recipes/recipes/" + name + ".recipe");
		this.name = itemToCraft;
		this.material = craftingMaterial;
		this.materialCount = materialCount;
	}
	
	public void save(){ //saves recipe if needed
		try {
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
	
	public String getName() {
		return name;
	}
	
	public void setName(String input) {
		this.name = input;
	}
	
	public String[] getCraftingMaterial() {
		return material;
	}
	
	public void setCraftingMaterial(String[] input) {
		this.material = input;
	}
	
	public int[] getCraftingMaterialCount() {
		return materialCount;
	}
	
	public void setCraftingMaterialCount(int[] input) {
		this.materialCount = input;
	}
	
}
