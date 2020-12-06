package com.brovko.maps;

import com.brovko.maps.component.Coordinate;
import com.brovko.maps.component.Parser;
import com.brovko.maps.component.Tile;
import com.brovko.maps.repositories.HeightRepo;
import com.brovko.maps.services.impl.CalcMaps;
import com.brovko.maps.services.impl.ElevationApiIO;
import com.brovko.maps.services.impl.FreeMapTools;
import com.brovko.maps.services.impl.Topocoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.System.exit;

@SpringBootApplication
public class SpringBootConsoleApplication implements CommandLineRunner {

	@Autowired
	public HeightRepo heightRepo;
	@Autowired
	public Parser parser;
	@Autowired
	public ElevationApiIO elevationApiIO;
	
	public static long start = System.currentTimeMillis();
	
	public static void main(String[] args) throws Exception {
		
		SpringApplication app = new SpringApplication(SpringBootConsoleApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
		
	}

	public void run(String... args) throws Exception {
		parser.run();
		System.out.println(System.currentTimeMillis() - start + " ms");
		exit(0);
	}
	
	public void run1(String... args) throws Exception {
		
		long start2 = System.currentTimeMillis();
		elevationApiIO.getHeight(7.719f, 19.688f);
		System.out.println(System.currentTimeMillis() - start2 + " ms");

		
		exit(0);
	}
	public void run2(String... args) throws Exception {

		long start2 = System.currentTimeMillis();
		Coordinate coordinateStart = new Coordinate(44.408168f, 38.503053f);
		Coordinate coordinateEnd = new Coordinate(44.351066f, 38.559055f);

		Tile tile = new Tile(coordinateStart, coordinateEnd, 4, heightRepo);
		tile.drawConsoleMatrix(10);

		//System.out.println();
		System.out.println(System.currentTimeMillis() - start + " ms");
		System.out.println(System.currentTimeMillis() - start2 + " ms");

		exit(0);
	}
}