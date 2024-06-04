import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';

class MovieRecommendation extends StatefulWidget {
  @override
  _MovieRecommendationState createState() => _MovieRecommendationState();
}

class _MovieRecommendationState extends State<MovieRecommendation> {
  List<dynamic> recommendedMovies = []; // 추천 영화 목록을 저장할 변수
  bool isLoading = true;

  @override
  void initState() {
    super.initState();
    fetchRecommendations();
  }

  void fetchRecommendations() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString('jwtToken');
    String? userId = prefs.getString('userid');

    // 토큰 또는 사용자 ID가 null인 경우 예외 처리
    if (token == null || userId == null) {
      print('Token or User ID is null');
      setState(() {
        isLoading = false;
      });
      return;
    }

    try {
      final response = await http.get(
        Uri.parse(
            'http://localhost:8080/api/movies/recommendations?userId=$userId'),
        headers: {'Authorization': 'Bearer $token'},
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        setState(() {
          recommendedMovies = data['content'];
          isLoading = false;
        });
      } else {
        throw Exception('Failed to load recommendations');
      }
    } catch (e) {
      print('Error fetching recommendations: $e');
      setState(() {
        isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('영화 추천'),
      ),
      body: isLoading
          ? Center(child: CircularProgressIndicator())
          : ListView.builder(
              itemCount: recommendedMovies.length,
              itemBuilder: (context, index) {
                var movie = recommendedMovies[index];
                return ListTile(
                  title: Text(movie['title']),
                  subtitle: Text(movie['overview']),
                  leading: Image.network(
                    'https://image.tmdb.org/t/p/w500${movie['poster_path']}',
                    fit: BoxFit.cover,
                    width: 100,
                  ),
                );
              },
            ),
    );
  }
}
