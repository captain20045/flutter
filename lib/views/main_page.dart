import 'package:flutter/material.dart';
import 'login_page.dart';
import 'signup_page.dart';
import 'mypage_page.dart';
import 'movie_view.dart';
import 'movie_recommend.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:http/http.dart' as http;

class MainPage extends StatefulWidget {
  @override
  _MainPageState createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {
  int _selectedIndex = 0;
  bool _isLoggedIn = false; // 로그인 상태를 저장하는 변수

  @override
  void initState() {
    super.initState();
    verifyTokenAndAutoLogout();
  }

  // 토큰 유효성 검사와 자동 로그아웃 처리
  void verifyTokenAndAutoLogout() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString('jwtToken');

    if (token != null) {
      var response = await http.get(
        Uri.parse('http://localhost:8080/verifyToken'),
        headers: {
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode == 200) {
        setState(() {
          _isLoggedIn = true;
        });
      } else {
        // 토큰이 만료되었거나 유효하지 않은 경우
        await prefs.setBool('isLoggedIn', false);
        await prefs.remove('jwtToken');
        setState(() {
          _isLoggedIn = false;
        });
      }
    } else {
      setState(() {
        _isLoggedIn = false;
      });
    }
  }

  // 로그아웃 메소드
  _logout() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.setBool('isLoggedIn', false);
    await prefs.remove('jwtToken'); // JWT 토큰을 SharedPreferences에서 제거

    Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (context) => MainPage()),
        (Route<dynamic> route) => false);
  }

  void _navigateToSelectedPage(int index) {
    setState(() {
      _selectedIndex = index;
    });

    if (!_isLoggedIn) {
      if (index == 1) {
        Navigator.push(
            context, MaterialPageRoute(builder: (context) => LoginPage()));
      } else if (index == 2) {
        Navigator.push(
            context, MaterialPageRoute(builder: (context) => SignUpPage()));
      }
    } else {
      switch (index) {
        case 0:
          Navigator.push(
              context,
              MaterialPageRoute(
                  builder: (context) => MovieView(isLoggedIn: true)));
          break;
        case 1:
          _logout();
          break;
        case 2:
          Navigator.push(
              context, MaterialPageRoute(builder: (context) => MyPage()));
          break;
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final List<BottomNavigationBarItem> _navItems = _isLoggedIn
        ? [
            BottomNavigationBarItem(
              icon: Icon(Icons.home),
              label: '홈',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.exit_to_app),
              label: '로그아웃',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.settings),
              label: '마이페이지',
            ),
          ]
        : [
            BottomNavigationBarItem(
              icon: Icon(Icons.home),
              label: '홈',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.login),
              label: '로그인',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.app_registration),
              label: '회원가입',
            ),
          ];

    return Scaffold(
      appBar: AppBar(
        title: Text('메인 페이지'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            ElevatedButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) => MovieView(isLoggedIn: _isLoggedIn)),
                );
              },
              child: Text('영화 목록'),
            ),
            if (_isLoggedIn) // 로그인 상태일 때만 영화 추천 버튼을 보여줌
              ElevatedButton(
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => MovieRecommendation()),
                  );
                },
                child: Text('영화 추천 받기'),
              ),
          ],
        ),
      ),
      bottomNavigationBar: BottomNavigationBar(
        items: _navItems,
        currentIndex: _selectedIndex,
        selectedItemColor: Colors.amber[800],
        onTap: _navigateToSelectedPage,
      ),
    );
  }
}
