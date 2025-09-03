# UFPR - TADS - DS151 DEV MOBILE 2025/2

- Integrantes
- ANDRE ANASTACIO DE OLIVEIRA
- DIEGO PUJOL ALVARES
- GIULIO MIGUEL SIMAO DA SILVA
- IAN BAILONE ALMEIDA


---

## 🚀 Como Rodar

1. **Clone** o repositório e abra no **Android Studio** (Giraffe+).
2. Garanta `minSdkVersion >= 28`.
3. Verifique as três Activities no `AndroidManifest.xml`.
4. Confirme que existem **>=10** drawables de bandeiras com os nomes esperados.
5. **Run** em um emulador ou dispositivo físico (API 28+).

---

## ✅ Critérios de Aceite

- [ ] Nome vazio **não** inicia o quiz.
- [ ] Quiz começa em “**1 of 5**” e nunca repete bandeira.
- [ ] Respostas são **case/accent-insensitive**.
- [ ] Após responder: mostra **feedback**, trava input e **habilita Next**.
- [ ] Cada acerto vale **+20**; total correto exibido no fim.
- [ ] Tela de Resultados mostra nome, `Score: X/100` e **5 linhas** (Correct/Incorrect).
- [ ] **Play Again** volta ao início sem duplicar pilha.
- [ ] Rotação não quebra o fluxo (não precisa restaurar tudo, mas **não pode crashar**).

---

## 🔎 Dicas de Teste Manual

- Fluxo com 5 acertos / 5 erros / misto.
- Nomes com espaços/acentos.
- Respostas com acentos e variação de maiúsculas (“México”, “MEXICO”, “mexico”).
- Ausência de algum drawable (Toast aparece e quiz continua).
- Vários ciclos de “Play Again”.

---
