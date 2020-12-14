package com.brovko.maps.utils;

import org.springframework.stereotype.Component;


@Component
public class ParallelCalcUtil {
	private final double EARTH_RADIUS = 6378137; // метров

	public double getLenghtParallel(float latitude) {
		return 2 * Math.PI * EARTH_RADIUS * Math.cos(latitude);


		/**
		 * Сделаем приближение №2. Считаем землю не сферой, а правильным эллипосоидом вращения.
		 * Экваториальный радиус a, полярный радиус b. Уравнение в цилиндрических координатах:
		 *
		 * r = a cos A
		 * z = b sin A,
		 *
		 * где r - радиус параллели, z - расстояние от точки до плоскости экватора, A - угол между радиус-вектором точки и плоскостью экватора.
		 *
		 * Проблема здесь вот в чём: угол A не является широтой. Широта - это угол Ф между плоскостью экватора и нормалью к поверхности эллипсоида в точке.
		 *
		 * Найдём угол наклона касательной к точке
		 * k1 = dz/dr =(dz/dA)/(dr/dA) = (b cos A)/(-a sin A) = -(b/a) ctg A
		 *
		 * Найдём угол наклона нормали
		 * k2 = -1/k1 = (a/b) tg A
		 *
		 * Угол наклона нормали - это и есть тангенс широты. Следовательно
		 *
		 * tg Ф = (a/b) tg A
		 *
		 * Откуда угол A:
		 *
		 * A = arctg [(b/a) tg Ф]
		 *
		 * Откуда длина параллели
		 *
		 * L = 2пr = 2пa cos A = 2пa cos {arctg [(b/a) tg Ф] }
		 */
	}
}
