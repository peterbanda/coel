package edu.banda.coel.web

class ChartData {

	class SeriesGroup {
		String groupLabel;
		List<String> labels = []
	}

	String title
	String xAxisCaption
	String yAxisCaption

	List<SeriesGroup> seriesGroups = []
//	List<String> seriesLabels = []
	List<List<Double>> series = []
	List<Double> xSteps = []
}