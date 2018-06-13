<?php
class db
{
		//private $conn = new mysqli($this->servername, $this->username, $this->password,$this->dbName);
		private $sql = "";
		function __construct(){
			echo($this->password);
			//Check Connection
			if ($conn->connect_error) {
				die("Connection failed: " . $conn->connect_error);
			} 
		}
		
		function __destruct(){
			if(isset($conn)){
				$conn->close();
			}
		}
		
		private function __get($constName){
			$val = null;
			switch($constName){
				case 'servername':
					$val = "localhost";
					break;
				case 'password':
					$val = "";
					break;
				case 'dbname':
					$val = "CSF";
					break;
			}
			return $val;
		}
		
		public function connect(){
			//Create Connection
			$conn = new mysqli($servername, $username, $password);
			//Check Connection
			if ($conn->connect_error) {
				die("Connection failed: " . $conn->connect_error);
			} 
		}
		
		public function createDB(){
			//Create DB
			$sql = "CREATE DATABASE CSF";
			//Check if worked
			if ($conn->query($sql) !== TRUE) {
				if($conn->errno!=1007){
					echo "Error creating database: " . $conn->errno;
				}
			}

			$conn = new mysqli($servername, $username, $password,$dbName);
			if ($conn->connect_error) {
				die("Connection failed: " . $conn->connect_error);
			} 
		}
		
		public function resetDB(){
			//Delete DB
			$sql = "DROP DATABASE CSF";
			//Check if worked
			if ($conn->query($sql) !== TRUE) {
				echo "Error deleting database: " . $conn->error;
			}
			//Create DB
			$sql = "CREATE DATABASE CSF";
			//Check if worked
			if ($conn->query($sql) !== TRUE) {
				if($conn->errno!=1007){
					echo "Error creating database: " . $conn->errno;
				}
			}

			$conn = new mysqli($servername, $username, $password,$dbName);
			if ($conn->connect_error) {
				die("Connection failed: " . $conn->connect_error);
			} 
		}
		
		public function createTable($name,$vals){
			$sql = "CREATE TABLE $name (";
			foreach($vals as $value){
				$sql .= $value . ", ";
			}
			$sql = substr($sql, 0, -1);
			$sql.=")";
			if($conn->query($sql)!==true){
				echo("Error adding table '$name': " . $conn->error);
			}
		}
}

echo("HI");
$test = new db();
$test->resetDB();
//Delete Database
/*
$sql = "CREATE TABLE users (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
firstname VARCHAR(30) NOT NULL,
lastname VARCHAR(30) NOT NULL,
email VARCHAR(50) NOT NULL,
password VARCHAR(50) NOT NULL,
reg_date TIMESTAMP
)";

$conn->query($sql);

$sql = "INSERT INTO users (firstname, lastname, email, password)
VALUES ('Zane', 'Clark', 'keterbusiness@gmail.com', 'hello')";

if($conn->query($sql)!==TRUE){
	echo("Error inserting into users: " . $conn->error);
	die();
}

$conn->close();*/
?>