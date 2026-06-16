package renderer;

import java.util.List;

import primitives.Point;

public class GridSampler implements Sampler {
	public List<List<Double>> sameplePoints(int nX, int nY) {
		List<List<Double>> points = new java.util.ArrayList<>();
		for (int i = -nX / 2; i < nX / 2; i++) {
			for (int j = -nY / 2; j < nY /2; j++) {
				List<Double> point = new java.util.ArrayList<>();
				point.add((i + 0.5) / nX);
				point.add((j + 0.5) / nY);
				points.add(point);
			}
		}
		return points;
	}
}
