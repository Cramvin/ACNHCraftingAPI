import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CraftingRecipeAPI {
	
	static File recipesFile = new File("recipes/recipes.txt"); // holds every name of items that can be crafted
	static List<String> recipes = new ArrayList<String>(); // used for checking if exists and creating recipes
	static File resourcesFile = new File("recipes/resources.txt"); // holds every name of items that are ingredients
	static List<String> resources = new ArrayList<String>(); // used for creating recipes
	
	static List<String> materials = new ArrayList<String>(); // holds every material that is used for the current recipe
	static List<Integer> countMaterial = new ArrayList<Integer>(); // how much material is used for the current recipe

	static void saveRecipe(String recipeName) throws IOException {
		
		recipes.add(recipeName);
		FileWriter fw = new FileWriter(recipesFile);
		BufferedWriter bw = new BufferedWriter(fw);
		for(String recipe : recipes) {
			bw.write(recipe);
			bw.newLine();
		}
		bw.close();
		
		File recipe = new File("recipes/recipes/"+recipeName+".txt");
		FileWriter fw2 = new FileWriter(recipe);
		BufferedWriter bw2 = new BufferedWriter(fw2);
		
		String[] material = materials.toArray(new String[materials.size()]);
		Integer[] materialCount = countMaterial.toArray(new Integer[countMaterial.size()]);
		
		for(int i = 0; i < material.length; i++)
			for(int j = 0; j < materialCount[i]; j++) {
				bw2.write(material[i]);
				bw2.newLine();
			}	
		
		bw2.close();
		materials = new ArrayList<String>();
		countMaterial = new ArrayList<Integer>();
	}

	static void newRecipe() {
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Wie heisst das Item, fuer das ein Rezept erstellt werden soll?");
		String recipeName = scan.next();
		System.out.println("Ist das Item \"" + recipeName + "\" richtig geschrieben?(y/n)");
		if(scan.next().equals("n")) {
			newRecipe();
			scan.close();
			return;
		}
		
		boolean wantContinue = false;
		do{

			System.out.println("Wie heisst das Material?");
			String materialName = scan.next();
			String correction = getAutoCorrection(materialName);
			if(correction != null) {
				System.out.println("Das Material \"" + materialName + "\" existiert nicht, soll stattdessen \"" + correction + "\" genutzt werden?(y/n)");
				if(scan.next().equals("y"))
					materialName = correction;
				else {
					newRecipe();
					scan.close();
					return;
				}
			}
			System.out.println("Wie viel davon wird benoetigt?");
			int count = Integer.parseInt(scan.next());
			
			materials.add(materialName);
			countMaterial.add(count);
			
			System.out.println("Besitzt das Rezept noch mehr Materialien?(y/n)");
			if(scan.next().equals("y")) {
				wantContinue = true;
			} else
				wantContinue = false;
		} while(wantContinue);

		try {
			saveRecipe(recipeName);
		} catch (IOException e) {
			System.out.println("Beim speichern des Rezeptes ist ein Fehler aufgetreten:" + e.toString());
		}
		newRecipe();
		scan.close();
	}

	static String getAutoCorrection(String arg) {
		
		for(String recipe : recipes)
			if(recipe.equals(arg)) {
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
	
	static String getAutoCorrectionRecipes(String arg, String closestEntry) {
		
		int closestLetterCount = 0;
		for(String recipe : recipes) {
			char[] charsRecipe = recipe.toCharArray();
			char[] charsArg = arg.toCharArray();
			int count = 0;
			int counting = charsRecipe.length > charsArg.length ? charsArg.length : charsRecipe.length;
			for(int i = 0; i < counting; i++)
				count += countUp(i, charsArg, charsRecipe);
			if(count > closestLetterCount) {
				closestLetterCount = count;
				closestEntry = recipe;
			}
		}
		return closestEntry;
		
	}
	
	static String getAutoCorrectionResources(String arg, String closestEntry) {
		
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

	private static int countUp(int i, char[] charsArg, char[] charsR) {
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

	static void getResources() throws IOException {
		
		if(!resourcesFile.exists()) {
			System.out.println("Im ordner \"recipes\" wird eine Datei \"recources.txt\" erwartet!");
			System.exit(0);
		}else {
			FileReader fr = new FileReader(resourcesFile);
			BufferedReader br = new BufferedReader(fr);
			String read = "";
			while((read = br.readLine()) != null)
				resources.add(read);
			br.close();
		}
		
	}

	static void getRecipes() throws IOException {

		if(!recipesFile.exists())
			recipesFile.createNewFile();
		else {
			FileReader fr = new FileReader(recipesFile);
			BufferedReader br = new BufferedReader(fr);
			String read;
			while((read = br.readLine()) != null)
				recipes.add(read);
			br.close();
		}
			
		
	}
	
}
