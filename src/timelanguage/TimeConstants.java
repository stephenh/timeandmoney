package timelanguage;


interface TimeConstants {
	int millisecondsPerSecond = 1000;
	int millisecondsPerMinute = 60 * millisecondsPerSecond;
	int millisecondsPerHour = 60 * millisecondsPerMinute;
	int millisecondsPerDay = 24 * millisecondsPerHour;
	int millisecondsPerWeek = 7 * millisecondsPerDay;

	int monthsPerQuarter = 3;
	int monthsPerYear = 12;
	int monthsPerDecade = 10 * monthsPerYear;
	int monthsPerCentury = 100 * monthsPerYear;
	int monthsPerMillenium = 1000 * monthsPerYear;
}
