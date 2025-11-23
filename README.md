# PORTAL-CURSO
Portal de Curso

# Converter Videos
- Baixe o zip:
https://www.gyan.dev/ffmpeg/builds/

- Coloque o bin/ no PATH do Windows

- Convertendo para HLS, execute o comando no diretório do video para gerar o arquivo:
  ffmpeg -i aula_1.mp4 \
  -profile:v baseline -level 3.0 -s 640x360 -start_number 0 \
  -hls_time 10 -hls_list_size 0 -f hls aula_1.m3u8




