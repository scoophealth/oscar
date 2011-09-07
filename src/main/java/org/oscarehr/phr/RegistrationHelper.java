package org.oscarehr.phr;

import java.util.Random;

import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.SpringUtils;

public final class RegistrationHelper {
	private static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private static Random random = new Random();

	public static String getDefaultUserName(int demographicId) {
		Demographic demographic = demographicDao.getDemographicById(demographicId);
		return((demographic.getFirstName() + '.' + demographic.getLastName()).toLowerCase());
	}

	/**
	 * Generate a password of length 12 using numbers and letters.
	 * This will ommit i/l/o/1/o to prevent abiguity.
	 * Due to the length the permutations are still large, i.e. (24^8 ~= 110 billion) * (8^4 = 4096) ~= 450,868,486,864,896 permutations ~= 450 trillion
	 */
	public static String getNewRandomPassword() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 4; i++) {
			// the specific purpose for enforcing a number after 2 letters is to prevent accidental rude words from being generated.
			// It's not a security concern that the sequence of numbers and letters is predictable as we could just make the string longer to increase the permutations.
			sb.append(getRandomPasswordLetter());
			sb.append(getRandomPasswordLetter());
			sb.append(getRandomPasswordDigit());
		}

		return (sb.toString());
	}

	/**
	 * @return digit from 2 to 9 inclusive.
	 */
	private static Object getRandomPasswordDigit() {
		// generate 0 to 7, then add 2, this will skip 0 and 1 so there's no ambiguity between 0/O and 1/I/l
		int i = random.nextInt(6);
		return (i + 2);
	}

	/**
	 * @return a lower case letter excluding i/l,o to prevent ambiguity with 1,0 respectively
	 */
	private static char getRandomPasswordLetter() {
		int i = random.nextInt('z' - 'a');
		i = i + 'a';

		if (i == 'i' || i == 'l' || i == 'o') return (getRandomPasswordLetter());

		return (char) (i);
	}
}