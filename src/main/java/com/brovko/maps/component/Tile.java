package com.brovko.maps.component;

import com.brovko.maps.model.Height;
import com.brovko.maps.repositories.HeightRepo;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;

public class Tile {
	
	public static int externalPrecision;
	private HeightRepo heightRepo;
	
	private float step;
	private int precision;
	private Coordinate coordinateStart;
	private Coordinate coordinateEnd;
	
	public Tile(Coordinate coordinateStart, Coordinate coordinateEnd) {
		this.coordinateStart = coordinateStart;
		this.coordinateEnd = coordinateEnd;
		this.precision = 3;
		this.step = 0.001f;
		externalPrecision = this.precision;
	}
	
	public Tile(Coordinate coordinateStart, Coordinate coordinateEnd, int precision, HeightRepo heightRepo) {
		this.coordinateStart = coordinateStart;
		this.coordinateEnd = coordinateEnd;
		this.precision = precision;
		this.heightRepo = heightRepo;
		this.step = (float) (1 / Math.pow(10, precision));
		externalPrecision = this.precision;
	}
	
	public LinkedList<Height> createMatrix() {
		
		List<Thread> threadList = new ArrayList<>();
		LinkedList<Height> heightLinkedList = new LinkedList();
		LinkedList<Coordinate> coordinateLinkedList = new LinkedList();
		
		double latitudeStart = customRound(Math.min(coordinateStart.getLatitude(), coordinateEnd.getLatitude()));
		double latitudeEnd = customRound(Math.max(coordinateStart.getLatitude(), coordinateEnd.getLatitude()));
		double longitudeStart = customRound(Math.min(coordinateStart.getLongitude(), coordinateEnd.getLongitude()));
		double longitudeEnd = customRound(Math.max(coordinateStart.getLongitude(), coordinateEnd.getLongitude()));
		
		HashMap<String, Height> heightHashMap = this.getMap(latitudeStart, latitudeEnd, longitudeStart, longitudeEnd, precision);
		//System.out.println(latitudeStart);
		//System.out.println(latitudeEnd);
		//System.out.println(longitudeStart);
		//System.out.println(longitudeEnd);
		
		for (double i = latitudeEnd; i > latitudeStart; i -= step) {
			for (double j = longitudeStart; j < longitudeEnd; j += step) {
				coordinateLinkedList.add(new Coordinate(customRound(i), customRound(j)));
			}
		}
		
		String mapKey;
		Iterator<Coordinate> iterator = coordinateLinkedList.iterator();
		while (iterator.hasNext()) {
			Coordinate coordinate = iterator.next();
			mapKey = coordinate.getMapKey();
			
		/*	if (heightHashMap.containsKey(mapKey)) {
				heightLinkedList.add(heightHashMap.get(mapKey));
			} else {
				try {
					Thread thread = new HeightWorkerThread(coordinate.getLatitude(), coordinate.getLongitude(), heightLinkedList, heightRepo);
					thread.setPriority(Thread.MAX_PRIORITY);
					thread.start();
					Thread.sleep(25);
					threadList.add(thread);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}*/
		}
		
		try {
			for (Thread thread : threadList) {
				thread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return heightLinkedList;
	}
	
	public void drawConsoleMatrix(int elevation) {
		LinkedList<Height> mapList = this.createMatrix();
		
		if (mapList.size() > 0) {
			
			LinkedList<Byte> bytePixelList = new LinkedList<>();
			Color color = Color.decode("#4D4DFF");
			
			int widthImage = 0;
			int heightImage = 0;
			double lastLatitude = 0;
			
			for (Height height : mapList) {
				if (height.getLatitude() != lastLatitude) {
					if (height.getLatitude() - lastLatitude > step * 0.9 || lastLatitude - height.getLatitude() > step * 0.9) {
						heightImage++;
						lastLatitude = height.getLatitude();
					}
				}
				
				if (height.getHeight() <= elevation) {
					bytePixelList.add((byte) color.getRed());
					bytePixelList.add((byte) color.getGreen());
					bytePixelList.add((byte) color.getBlue());
					bytePixelList.add((byte) -127);
				} else {
					bytePixelList.add((byte) 0);
					bytePixelList.add((byte) 0);
					bytePixelList.add((byte) 0);
					bytePixelList.add((byte) 0);
				}
				if (heightImage == 1)
					widthImage++;
			}
			
			System.out.println("widthImage " + widthImage);
			System.out.println("heightImage " + heightImage);
			
			byte[] bigArray = new byte[bytePixelList.size()];
			
			Iterator<Byte> iteratorByte = bytePixelList.iterator();
			int position = 0;
			while (iteratorByte.hasNext()) {
				bigArray[position] = iteratorByte.next();
				position++;
			}
			System.out.println("bigArray " + bigArray.length);
			
			DataBuffer buffer = new DataBufferByte(bigArray, bigArray.length);
			
			WritableRaster raster = Raster.createInterleavedRaster(buffer, widthImage, heightImage, 4 * widthImage, 4, new int[]{0, 1, 2, 3}, (Point) null);
			ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), true, true, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
			BufferedImage image = new BufferedImage(cm, raster, true, null);
			
			try {
				ImageIO.write(image, "png", new File("image.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			/*try {
				int width = 255;
				int height = 255;
				
				
				int tileLenght = width * height * step;
				
				byte[] bigArray = new byte[tileLenght];
				Color color = Color.decode("#4D4DFF");
				boolean flag = false;
				for (int i = 0; i < tileLenght; i++) {
					if (flag) {
						bigArray[i] = (byte) color.getRed();
						bigArray[++i] = (byte) color.getGreen();
						bigArray[++i] = (byte) color.getBlue();
						flag = true;
					} else {
						bigArray[i] =  0;
						bigArray[++i] = 0;
						bigArray[++i] = 0;
						flag = true;
					}
				}
				
				
			}catch (Exception ex){}
			*/
			
			/*Iterator<Height> iterator = mapList.iterator();
			double lastLatitude = 0;
			while (iterator.hasNext()) {
				Height height = iterator.next();
				
				if (height.getLatitude() != lastLatitude) {
					if (height.getLatitude() - lastLatitude > step * 0.9 || lastLatitude - height.getLatitude() > step * 0.9) {
						System.out.println();
						lastLatitude = height.getLatitude();
					}
				}
				
				if (height.getHeight() > elevation) {
					System.out.print("X ");
				} else {
					System.out.print(". ");
				}
			}*/
		}
	}
	
	private float customRound(float num) {
		return new BigDecimal(num).setScale(precision, RoundingMode.HALF_UP).floatValue();
	}

	private float customRound(double num) {
		return new BigDecimal(num).setScale(precision, RoundingMode.HALF_UP).floatValue();
	}
	
	/**
	 * Поиск осуществляется по координатам
	 * - по широте  по убыванию  (сверху вниз)
	 * - по долготе возрастанию ( слево на право)
	 */
	public HashMap<String, Height> getMap(double latitudeStart, double latitudeEnd, double longitudeStart, double longitudeEnd, int precision) {
		
		HashMap<String, Height> hashMap = new HashMap<>();
		
		List<Height> heightList = heightRepo.fetchTile(latitudeStart, latitudeEnd, longitudeStart, longitudeEnd);
		
		Iterator iterator = heightList.iterator();
		while (iterator.hasNext()) {
			Height point = (Height) iterator.next();
			
			double latPoint = customRound(point.getLatitude());
			double lonPoint = customRound(point.getLongitude());
			
			String key = latPoint + "_" + lonPoint;
			if (!hashMap.containsKey(key)) {
				hashMap.put(key, point);
			}
		}
		
		return hashMap;
	}
}
