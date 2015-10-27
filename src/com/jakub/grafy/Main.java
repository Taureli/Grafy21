package com.jakub.grafy;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	static ArrayList<ArrayList<?>> matrix = new ArrayList<ArrayList<?>>();
	static int minLength;	//minimalna d�ugo�� szukanego cyklu
	
	public static void main(String args[]) throws Exception{
		int command = -1;
		Scanner scan = new Scanner(System.in);
		
		while(command != 0){
			System.out.println("\n0 - zako�cz program;");
			System.out.println("1 - za�aduj macierz z pliku;");
			System.out.println("2 - znajd� �cie�k� cylku;");
			System.out.println("Wybierz polecenie: ");
			command = scan.nextInt();
			
			switch(command){
			case(1):
				loadMatrix();
				System.out.println("Wczytano macierz o minimalnym stopniu: " + minDeg());
				break;
			case(2):
				getCycle();
				break;
			}
		}
		scan.close();
	}
	
	//Wczytywanie macierzy z pliku
		@SuppressWarnings({ "unchecked" })
		public static void loadMatrix() throws Exception {
			File file = new File("Macierz.txt");
			Scanner sc = new Scanner(file);
			matrix.clear();

			//Tworz� macierz
			int lineCount = 0;
			while (sc.hasNextLine()) {
				String[] currentLine = sc.nextLine().trim().split(" "); 
				matrix.add(new ArrayList<Object>());
				for (int i = 0; i < currentLine.length; i++) {
					((ArrayList<Integer>)matrix.get(lineCount)).add(Integer.parseInt(currentLine[i]));
				}
				lineCount++;
			}  
			sc.close();
		}
		
		//Stopie� wierzcho�ka
		@SuppressWarnings("unchecked")
		public static int vertDeg(int vert){
			int deg;
			int loops = 0;
			int edges = 0;
			
			for(int i = 0; i < matrix.size(); i++){
				if(i == vert)
					loops = ((ArrayList<Integer>)matrix.get(vert)).get(i);
				else
					edges += ((ArrayList<Integer>)matrix.get(vert)).get(i);
			}
			
			deg = 2 * loops + edges;
			return deg;
		}
		
		//Stopie� minimalny
		public static int minDeg(){
			int min = vertDeg(0);
			int temp;
			
			for(int i = 0; i < matrix.size(); i++){
				temp = vertDeg(i);
				if(temp < min)
					min = temp;
			}
			
			return min;
		}
		
		//Znajdowanie cyklu d�ugo�ci >= min+1
		public static void getCycle(){
			minLength = minDeg() + 1;
			ArrayList<Integer> path = new ArrayList<Integer>();
			
			path.add(0);	//Zaczynam od pierwszego wierzcho�ka
			if( createCycle(path, 0, 1) ){
				System.out.println("�cie�ka: ");
				for(int i = 0; i < path.size(); i++){
					System.out.print((path.get(i)) + " -> ");
				}
			} else {
				System.out.println("B��d: Nie znaleziono cyklu!");
			}
		}
		
		//Funkcja rekursywna do wyznaczania �cie�ki cyklu
		@SuppressWarnings("unchecked")
		public static boolean createCycle(ArrayList<Integer> path, int previousVert, int position){
			
			//Sprawdzam czy d�ugo�� jest spe�niona i czy istnieje
			//kraw�d� z ostatniego elementu do pocz�tku �cie�ki
			if(position >= minLength && ((ArrayList<Integer>)matrix.get(previousVert)).get(path.get(0)) > 0){
				return true;
			}
			
			//Je�li dojdziemy do ko�ca i nie znajdziemy cyklu
			if(position == matrix.size() && ((ArrayList<Integer>)matrix.get(previousVert)).get(path.get(0)) == 0){
				return false;
			}
			
			//Szukam s�siad�w
			for(int i = 0; i < matrix.size(); i++){
				if(((ArrayList<Integer>)matrix.get(previousVert)).get(i) > 0 && !path.contains(i)){
					path.add(i);
					if(createCycle(path, i, position + 1)){
						return true;
					}else{
						path.remove(position);
					}	
				}
			}
			
			return false;
		}
}
