<?php
$servername = "127.0.0.1";
$name = "root";
$password = "123456";
$dbname = "dianshichengjin";

// 创建连接
$conn = new mysqli($servername, $name, $password, $dbname);
// 检测连接
if ($conn->connect_error) {
    die("connect failed: " . $conn->connect_error);
}

$json = file_get_contents('php://input');
$arr = json_decode($json, true);


$userId = $arr['userId'];
$activityId = $arr['activityId'];
$remarks = $arr['remarks'];



$sql = "INSERT INTO participate (userId, activityId, remarks)
VALUES ( '".$userId."' , '".$activityId."' ,'".$remarks."')";

$response = array("code"=>"404","mseeage"=>"failed","data"=>"none");

if ($conn->query($sql) === TRUE) {
    $response['code'] = 200;
    $response['mseeage'] = "success";
    $response['data'] = "review success";
    echo json_encode($response);
} else {
    $response['data'] = "Error: " . $sql . "<br>" . $conn->error;
    echo json_encode($response);
}

$conn->close();
?> 