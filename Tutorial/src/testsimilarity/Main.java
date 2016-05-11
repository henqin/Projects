package testsimilarity;

public class Main
{

	public static double similar(String target, String source)
	{
		SimilarityStrategy strategy = new LevenshteinDistanceStrategy();
		StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
		double score = service.score(source, target);
		// System.out.println("score is "+score);
		return score;
	}

	// public static void main(String[] args) {
	// SimilarityStrategy strategy = new LevenshteinDistanceStrategy();
	// String target = "Piccadily";
	// String source = "Piccadiiy";
	// StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
	// double score = service.score(source, target); // Score is 0.90
	// System.out.println("score is "+score);
	// }

}
