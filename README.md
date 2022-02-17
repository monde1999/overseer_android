# overseer_android
This is the new version of Overseer.

## Setup
1. Run cmd/ipconfig to get host IP address. Example: 192.168.1.2

2. Make sure to add the host IP address to the ALLOWED_HOSTS in settings.py\
   Example: ALLOWED_HOSTS = ["192.168.1.2"] or ALLOWED_HOSTS = ["*"] 

3. Use the host IP address in running the server.\
   Example: python manage.py runserver 192.168.1.2:8000

4. In the login screen, click the pen icon on the top-right corner to edit the IP address.\
   Example: http://192.168.1.2:8000/ \
   Click OK to save. This will be loaded on future app start up.

