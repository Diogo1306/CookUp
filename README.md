# CookUp 🍳

Monorepo do projeto **CookUp** — aplicação de receitas desenvolvida como PAP por Diogo Esteves.
Junta num só repositório a app Android, a API, o dashboard de administração e o backend de notificações, com todo o histórico de commits dos repositórios originais preservado.

## Estrutura

| Pasta | O que é | Tecnologias |
|---|---|---|
| [`app/`](app/) | Aplicação Android | Java, Gradle |
| [`api/`](api/) | API / backend principal | PHP, MySQL |
| [`dashboard/`](dashboard/) | Dashboard de administração | React, Vite, Material UI |
| [`firebase-admin/`](firebase-admin/) | Backend de notificações | Node.js, Firebase Admin SDK |
| [`docs/`](docs/) | Manual de instalação (PDF) | — |
| [`assets/icons/`](assets/icons/) | Ícones e imagens da marca | — |

## Como correr

### API (`api/`)
Requer PHP + MySQL (ex.: XAMPP/WAMP). Configura a base de dados e o IP local em `api/config/config.php`.

### App Android (`app/`)
Abrir a pasta `app/` no Android Studio e sincronizar o Gradle. Ajustar o endereço da API para o IP da máquina onde a API está a correr.

### Dashboard (`dashboard/`)
```bash
cd dashboard
npm install
npm run dev
```

### Firebase Admin (`firebase-admin/`)
```bash
cd firebase-admin
npm install
node index.js
```
> ⚠️ Requer um `serviceAccountKey.json` (chave privada do Firebase). Este ficheiro **não está no repositório** por segurança — obtém-no na consola do Firebase (Definições do projeto → Contas de serviço) e coloca-o em `firebase-admin/`.

## Histórico

Este monorepo foi criado a partir de três repositórios originais, com o histórico completo importado via `git filter-repo`:

- `Diogo1306/CookUp` → `app/`
- `Diogo1306/CookUp_Core` → `api/`
- `Diogo1306/CookUp_Dasboard` → `dashboard/`

Os repositórios originais foram arquivados.

## Licença

**Todos os direitos reservados.** Este repositório está público apenas para visualização (portefólio). Não é permitido copiar, modificar ou usar este código ou a ideia do projeto — incluindo para outras PAP ou trabalhos académicos — sem autorização escrita do autor. Ver [LICENSE](LICENSE).
