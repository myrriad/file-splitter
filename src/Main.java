import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
	
		
		try (
			BufferedReader reader = new BufferedReader(new FileReader("out/pic.jpg-01"));
				BufferedReader breader = new BufferedReader(new FileReader("pic.jpg"));
				BufferedWriter writer = new BufferedWriter(new FileWriter("mergeout/pic.jpg"))
			) 
		{
			String str = reader.readLine();
			System.out.println(str.substring(420));
			System.out.println(breader.readLine().substring(420));
			str = breader.readLine();
			while(str!=null) {
				writer.write(str);
				System.out.println(str);
				str = breader.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//*/
		//	System.out.println(prependZeroes(4,16));
	//	splitFile("out/source","source.txt",1000);
	//	mergeFiles("pic.jpg", "out");
	}

	
	/**
	 * Splits a file into many files.
	 *  
	 * Each file is name (original file name)-(piece number) 
	 * 
	 * For example, image.jpg might be split into iamge.jpg-01, image.jpg-02, etc.
	 * 
	 * @param outfolder The folder where the output will be spit out.
	 * @param infile The file which the program will split into tinny pieces. 
	 * @param cutSize The size of each tiny file.
	 */
	public static void splitFile(String outfolder, String infile, int cutSize) {
		byte[][] retn = new byte[1][1];

		try {

			Path opath = Paths.get(infile);
			String inname = opath.getFileName().toString();
			
			/**arraysize limited by integer.maxvalue, which makes the largest file ~2TB*/
			byte[] bytes = Files.readAllBytes(opath);

			/**The number of bytes left to parse*/
			int remaining = bytes.length; 
			
			/**how many files will be created*/
			int initlen = (int) ceil(bytes.length, cutSize);

			/**is the cut marker */
			int i = 0;
			
			/**counts from 0 to initlen; the file index*/
			int j = 0;

			

			// ensures that the outfolder exists
			new File(outfolder).mkdirs();
			Path path = Paths.get(outfolder + "/" + inname + "-" + prependZeroes(j + 1, initlen));
			while (remaining > cutSize) {

				Files.write(path, Arrays.copyOfRange(bytes, i, i + cutSize));

				remaining -= cutSize;

				i += cutSize;
				j++;

				path = Paths.get(outfolder + "/" + inname + "-" + prependZeroes(j + 1, initlen));
			}
			Files.write(path, Arrays.copyOfRange(bytes, bytes.length - remaining, bytes.length));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			int i = 0;
			for (byte[] aby : retn) {
				for (byte b : aby) {
					System.out.print((char) b);
				}
				System.out.println(i);
				i++;
			}
			e.printStackTrace();
		}

	}

	public static void mergeFiles(String outfile, String infolder) {
		File ifile = new File(infolder);
		
		File ofile = new File("mergeout/",outfile);
		ofile.getParentFile().mkdirs();
		try {
			ofile.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(ofile))) {
			for (File file : ifile.listFiles()) {

				if (file.isFile() && file.getName().substring(0,file.getName().lastIndexOf('-')).equals(outfile)) {
				System.out.println(file.getName());
				try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
						String str = reader.readLine();

						
						while (str != null) {
							
							writer.write(str);
							str = reader.readLine();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void copyTo(File original, File to) {

	}

	public static int ceil(int numerator, int denominator) {
		return numerator % denominator == 0 ? numerator / denominator : numerator / denominator + 1;
	}
	
	public static String prependZeroes(int num, int totalcount) {
		/**converts totalcount into the string, then gets the length to find the places
		 * ie. 45 -> "45"; length = 2; append accordingly
		 * */
		int places = (""+totalcount).length();
		if(places==2) return (num<10?"0":"")+num;
		else return (num<simpow(10, places-1)?"0":"")+num;
	}
	public static int simpow(int a, int power) {
		int retn = a;
		for(int i=1;i<power;i++) {
			retn*=a;
		}
		return retn;
	}
}
