# PORTAL-CURSO
Portal de Curso

# Converter Videos
- Baixe o zip:
https://www.gyan.dev/ffmpeg/builds/

- Coloque o bin/ no PATH do Windows

- Convertendo para HLS, execute o comando no diretório do video para gerar o arquivo:
  ffmpeg -i aula_1.mp4 -c:v libx264 -preset slow -crf 18 -g 48 -keyint_min 48 -sc_threshold 0 -c:a aac -b:a 192k -f hls -hls_time 6 -hls_playlist_type vod aula_1.m3u8



