import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Test {

	public static void main(String[] args) {
		Set<Integer> uniqueDemographicIdsWithSomethingNew = new HashSet<Integer>();
		uniqueDemographicIdsWithSomethingNew.add(1);
		uniqueDemographicIdsWithSomethingNew.add(1);
		
		List<Integer> vals = new ArrayList<Integer>();
		vals.add(1);
		vals.add(1);
		
		uniqueDemographicIdsWithSomethingNew.addAll(vals);
		
		System.out.println(uniqueDemographicIdsWithSomethingNew);
	}

}
