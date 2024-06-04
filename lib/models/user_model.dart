class SignUpModel {
  String name;
  String gender;
  String userid;
  String userpw;
  String email;
  String phone_number;

  SignUpModel({
    required this.name,
    required this.gender,
    required this.userid,
    required this.userpw,
    required this.email,
    required this.phone_number,
  });

  // JSON 데이터를 SignUpModel 객체로 변환하는 팩토리 생성자
  factory SignUpModel.fromJson(Map<String, dynamic> json) {
    return SignUpModel(
      name: json['name'],
      gender: json['gender'],
      userid: json['userid'],
      userpw: json['userpw'],
      email: json['email'],
      phone_number: json['phone_number'],
    );
  }

  // SignUpModel 객체를 JSON 데이터로 변환하는 메서드
  Map<String, dynamic> toJson() {
    return {
      'name': name,
      'gender': gender,
      'userid': userid,
      'userpw': userpw,
      'email': email,
      'phone_number': phone_number,
    };
  }
}
