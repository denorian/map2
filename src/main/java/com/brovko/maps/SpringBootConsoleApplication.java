package com.brovko.maps;

import com.brovko.maps.component.Coordinate;
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
		System.out.println(heightRepo);
		Coordinate coordinateStart = new Coordinate(44.398168, 38.513053);
		Coordinate coordinateEnd = new Coordinate(44.351066, 38.559055);
		
		Tile tile = new Tile(coordinateStart, coordinateEnd, 4, heightRepo);
		tile.drawConsoleMatrix(10);
		
		//System.out.println();
		System.out.println(System.currentTimeMillis() - start + " ms");
		System.out.println(System.currentTimeMillis() - start2 + " ms");
		
		/*HeightComponent heightComponent = new HeightComponent(heightRepo);
		for (double i = 33; i < 34; i += 0.0001) {
			System.out.println(heightComponent.getHeight(i,84.211));
		}*/
		exit(0);
	}
}