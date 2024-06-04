class Movie {
  final int id;
  final String title;
  final String overview;
  final double? vote_average; // 평점은 null일 수도 있으므로, nullable 타입으로 선언
  final String poster_path;
  final String release_date;

  Movie({
    required this.id,
    required this.title,
    required this.overview,
    this.vote_average,
    required this.poster_path,
    required this.release_date,
  });

  // JSON 형태의 데이터를 받아 Movie 객체로 변환하는 팩토리 생성자
  factory Movie.fromJson(Map<String, dynamic> json) {
    return Movie(
      id: json['id'],
      title: json['title'],
      overview: json['overview'],
      vote_average: json['vote_average'].toDouble(), // JSON에서 받은 값을 double로 변환
      poster_path: json['poster_path'],
      release_date: json['release_date'],
    );
  }
}
