import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';

class MovieView extends StatefulWidget {
  final bool isLoggedIn;

  MovieView({required this.isLoggedIn});

  @override
  _MovieViewPageState createState() => _MovieViewPageState();
}

class _MovieViewPageState extends State<MovieView> {
  List<dynamic> movies = [];
  Map<int, bool> _watchedMovies = {};
  ScrollController _scrollController = ScrollController();
  TextEditingController _searchController = TextEditingController();
  bool isLoading = false;
  int currentPage = 0;

  @override
  void initState() {
    super.initState();
    fetchMovies(); // 초기 영화 데이터 로드
    fetchViewedMovies(); // 사용자가 시청한 영화 정보를 로드
    _scrollController.addListener(() {
      if (_scrollController.position.pixels ==
              _scrollController.position.maxScrollExtent &&
          !isLoading) {
        fetchMovies(); // 추가 데이터 로드
      }
    });
  }

  void fetchMovies() async {
    if (isLoading) return;
    setState(() => isLoading = true);

    String url = _searchController.text.isEmpty
        ? 'http://localhost:8080/api/movies/all?page=$currentPage&size=10'
        : 'http://localhost:8080/api/movies/search?query=${_searchController.text}&page=$currentPage&size=10';

    final response = await http.get(Uri.parse(url));

    if (response.statusCode == 200) {
      final data = json.decode(response.body);
      if (data['content'].isNotEmpty) {
        setState(() {
          movies.addAll(data['content']);
          currentPage++; // 데이터가 추가로 로드될 때만 페이지 번호를 증가
        });
      }
    } else {
      showSnackBar('데이터를 불러오는데 실패했습니다.');
    }
    setState(() => isLoading = false);
  }

  void fetchViewedMovies() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? userId = prefs.getString('userid');
    String? token = prefs.getString('jwtToken');

    if (token == null || userId == null) {
      print('Auth token or user ID is not available.');
      return;
    }

    var url = Uri.parse('http://localhost:8080/api/viewedMovies/$userId');
    var response = await http.get(
      url,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode == 200) {
      List<dynamic> viewedMovies = json.decode(response.body);
      print("Received viewed movies: $viewedMovies");

      setState(() {
        viewedMovies.forEach((movie) {
          _watchedMovies[movie['movieId']] = true;
        });
      });
    } else {
      print('Failed to fetch viewed movies. Status: ${response.statusCode}');
      print('Error Body: ${response.body}');
    }
  }

  void searchMovies(String query) {
    setState(() {
      movies.clear();
      currentPage = 0; // 검색을 시작할 때 페이지를 초기화
      _searchController.text = query; // 검색어 업데이트
    });
    fetchMovies();
  }

  void showSnackBar(String message) {
    ScaffoldMessenger.of(context)
        .showSnackBar(SnackBar(content: Text(message)));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('영화 목록'),
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.search),
            onPressed: () => searchMovies(_searchController.text),
          ),
        ],
        bottom: PreferredSize(
          preferredSize: Size.fromHeight(48.0),
          child: TextField(
            controller: _searchController,
            decoration: InputDecoration(
              labelText: '검색...',
              prefixIcon: Icon(Icons.search),
              border: OutlineInputBorder(),
            ),
            onSubmitted: searchMovies,
          ),
        ),
      ),
      body: isLoading
          ? Center(child: CircularProgressIndicator())
          : ListView.builder(
              key: PageStorageKey('movieList'),
              itemCount: movies.length,
              itemBuilder: (context, index) => buildMovieCard(movies[index]),
              controller: _scrollController,
            ),
    );
  }

  Widget buildMovieCard(dynamic movie) {
    bool isWatched =
        _watchedMovies[movie['id']] ?? false; // 'movie['id']'는 각 영화의 고유 ID입니다.

    return Card(
      clipBehavior: Clip.antiAlias,
      child: ListTile(
        title: Text(movie['title']),
        subtitle: Text(movie['overview']),
        leading: movie['poster_path'] != null
            ? Image.network(
                'https://image.tmdb.org/t/p/w500${movie['poster_path']}',
                fit: BoxFit.cover,
                width: 100,
              )
            : SizedBox(width: 100, height: 100, child: Placeholder()),
        trailing: widget.isLoggedIn && !isWatched
            ? TextButton(
                onPressed: () {
                  _recordViewingHistory(movie['id']);
                },
                child: Text('보기'),
                style: ButtonStyle(
                  foregroundColor:
                      MaterialStateProperty.all<Color>(Colors.white),
                  backgroundColor:
                      MaterialStateProperty.all<Color>(Colors.blue),
                  padding: MaterialStateProperty.all(EdgeInsets.all(10.0)),
                ),
              )
            : null,
      ),
    );
  }

  void _recordViewingHistory(int movieId) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? userId = prefs.getString('userid');
    String? token = prefs.getString('jwtToken');

    if (token == null || userId == null) {
      print('Auth token or user ID is not available.');
      return;
    }

    var url = Uri.parse('http://localhost:8080/api/recordViewingHistory');
    var response = await http.post(
      url,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
      body: json.encode({
        'userId': userId,
        'movieId': movieId,
        'viewingDate': DateTime.now().toIso8601String(),
      }),
    );

    if (response.statusCode == 200) {
      print('Viewing history recorded successfully.');
      fetchViewedMovies(); // Fetch updated viewing history after recording
    } else {
      print('Failed to record viewing history. Status: ${response.statusCode}');
    }
  }
}
