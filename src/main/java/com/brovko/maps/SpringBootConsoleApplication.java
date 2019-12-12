package com.brovko.maps;

import com.brovko.maps.component.Coordinate;
import com.brovko.maps.component.Parser;
import com.brovko.maps.component.Tile;
import com.brovko.maps.repositories.HeightRepo;
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
	
	public static long start = System.currentTimeMillis();
	
	public static void main(String[] args) throws Exception {
		
		SpringApplication app = new SpringApplication(SpringBootConsoleApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
		
	}
	
	@Override
	public void run(String... args) throws Exception {
		
		long start2 = System.currentTimeMillis();
		
		Parser parser = new Parser(heightRepo,0.1, 2);
		parser.run();
		
		System.out.println(System.currentTimeMillis() - start + " ms");
		System.out.println(System.currentTimeMillis() - start2 + " ms");
		
		exit(0);
	}
	
	public void run2(String... args) throws Exception {
		
		long start2 = System.currentTimeMillis();
		System.out.println(heightRepo);
		Coordinate coordinateStart = new Coordinate(44.408168, 38.503053);
		Coordinate coordinateEnd = new Coordinate(44.351066, 38.559055);
		
		Tile tile = new Tile(coordinateStart, coordinateEnd, 4, heightRepo);
		tile.drawConsoleMatrix(10);
		
		//System.out.println();
		System.out.println(System.currentTimeMillis() - start + " ms");
		System.out.println(System.currentTimeMillis() - start2 + " ms");
		
		exit(0);
	}
}