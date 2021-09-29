# overseer_android
This is the new version of Overseer.

## Setup
1. In Constants.java, change BASE_URL_OVERSEER accordingly.\
   Example: public static String BASE_URL_OVERSEER = "http://192.168.1.11:8000/";
   
2. Make sure to add the IP address to the ALLOWED_HOSTS in settings.py\
   Example: ALLOWED_HOSTS = ["192.168.1.11"] or ALLOWED_HOSTS = ["*"]
   
3. Use the IP address of BASE_URL_OVERSEER in running the server.\
   Example: python manage.py runserver 192.168.1.11:8000
