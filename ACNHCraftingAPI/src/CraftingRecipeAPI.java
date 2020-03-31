import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CraftingRecipeAPI {
	
	private File recipesFile = new File("recipes/recipes.txt"); // holds every name of items that can be crafted
	private List<Recipe> recipes = new ArrayList<Recipe>(); // list of all recipes used for checking if exists and creating recipes
	private File resourcesFile = new File("recipes/resources.txt"); // holds every name of items that are ingredients
	private List<String> resources = new ArrayList<String>(); // used for creating recipes

	public CraftingRecipeAPI() {
		getResources();
		getRecipes();
	}
	
	private void saveRecipe(String recipeName) { // saves a specific recipe from the recipes list
		
		for(Recipe recipe : recipes) {
			if(recipe.getName().equals(recipeName))
				recipe.save();
		}
		
	}

	void newRecipe(String name, String[] material, int[] materialCount) { // creates a new recipe and saves it
		
		recipes.add(new Recipe(name, material, materialCount));
		saveRecipe(name);
		try {
			FileWriter fw = new FileWriter(recipesFile);
			BufferedWriter bw = new BufferedWriter(fw);
			for(Recipe recipe : recipes) {
				bw.write(recipe.getName());
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			System.out.println("It was not possible to create an entry for " + name + ": " + e.toString());
		}
		
	}

	String getAutoCorrection(String arg) { // checks if there is an item with this name or corrects it
		
		for(Recipe recipe : recipes)
			if(recipe.getName().equals(arg)) {
				return null;
			}
		for(String resource : resources)
			if(resource.equals(arg)) {
				return null;
			}
		String closestEntry = "(starte das Programm bitte jetzt neu)";
		String in = getAutoCorrectionRecipes(arg, closestEntry);
		if(!in.equals(closestEntry))
			return in;
		in = getAutoCorrectionResources(arg, closestEntry);
		if(!in.equals(closestEntry))
			return in;
		return closestEntry;
		
	}
	
	String getAutoCorrectionRecipes(String arg, String closestEntry) {  // checks if there is a recipe with this name or corrects it
		
		int closestLetterCount = 0;
		for(Recipe recipe : recipes) {
			char[] charsRecipe = recipe.getName().toCharArray();
			char[] charsArg = arg.toCharArray();
			int count = 0;
			int counting = charsRecipe.length > charsArg.length ? charsArg.length : charsRecipe.length;
			for(int i = 0; i < counting; i++)
				count += countUp(i, charsArg, charsRecipe);
			if(count > closestLetterCount) {
				closestLetterCount = count;
				closestEntry = recipe.getName();
			}
		}
		return closestEntry;
		
	}
	
	String getAutoCorrectionResources(String arg, String closestEntry) {  // checks if there is a resource with this name or corrects it
		
		int closestLetterCount = 0;
		for(String resource : resources) {
			char[] charsResource = resource.toCharArray();
			char[] charsArg = arg.toCharArray();
			int count = 0;
			int counting = charsResource.length > charsArg.length ? charsArg.length : charsResource.length;
			for(int i = 0; i < counting; i++)
				count += countUp(i, charsArg, charsResource);
			if(count > closestLetterCount) {
				closestLetterCount = count;
				closestEntry = resource;
			}
		}
		return closestEntry;
		
	}

	private int countUp(int i, char[] charsArg, char[] charsR) { // needed for the auto correction
		int count = 0;
		if(charsArg[i] == charsR[i])
			count++;
		if(i >= 1) {
			if(charsArg[i] == charsR[i-1])
				count++;
		}
		if(i <= 1) {
			if(charsArg[i] == charsR[i+1])
				count++;
		}
		return count;
	}

	private void getResources() { // gets all the names for resources
		
		if(!resourcesFile.exists()) {
			System.out.println("File \"recipes/recources.txt\" not found!");
			System.exit(0);
		}else {
			try {
				FileReader fr = new FileReader(resourcesFile);
				BufferedReader br = new BufferedReader(fr);
				String read = "";
				while((read = br.readLine()) != null)
					resources.add(read);
				br.close();
			} catch (IOException e) {
				System.out.println("Could not load resources properly: " + e.toString());
			}
		}
		
	}

	private void getRecipes() { // gets all recipes

		if(recipesFile.exists()) 
			try {
				FileReader fr = new FileReader(recipesFile);
				BufferedReader br = new BufferedReader(fr);
				String read;
				while((read = br.readLine()) != null) { // for every recipe...
					File recipeFile = new File("recipes/recipes/" + read + ".recipe");
					FileReader fr2 = new FileReader(recipeFile);
					BufferedReader br2 = new BufferedReader(fr2);
					
					List<String> material = new ArrayList<String>();
					List<Integer> materialCount = new ArrayList<Integer>(); 
					String read2; 
					int count = 0;
					
					while((read2 = br2.readLine()) != null) { // ...go through all materials and counts...
						if(material.contains(read2)) {
							count++;
						} else {
							material.add(read2);
							if(count != 0) {
								materialCount.add(count);
								count = 0;
							}
							count++;
						}
					}
					materialCount.add(count);
					
					int[] materialCountArray = new int[materialCount.size()];
					int i = 0;
					for(Integer materialC : materialCount) {
						materialCountArray[i] = materialC; i++;
					}
					recipes.add(new Recipe(read, material.toArray(new String[material.size()]), materialCountArray)); // ...and finally save it
					br2.close();
				}
				br.close();
			} catch (IOException e) {
				System.out.println("Could not load recipes properly: " + e.toString());
			}
			
	}
	
}
