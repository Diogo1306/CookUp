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
| [`assets/icons/`](assets/icons/) | Ícones e imagens da marca | — |

## Instalação e execução

### 1. API (`api/`)
Requer PHP + MySQL (ex.: XAMPP ou WAMP).

1. Copiar a pasta `api/` para o diretório do servidor web (ex.: `htdocs/`).
2. Criar a base de dados e importar `api/config/cookup_db.sql` (ex.: via phpMyAdmin).
3. Em `api/config/config.php`, ajustar as credenciais da base de dados e o `BASE_URL` para o IP da tua máquina (vê o IPv4 com `ipconfig`).

### 2. App Android (`app/`)
1. Abrir a pasta `app/` no Android Studio e sincronizar o Gradle.
2. Ajustar o endereço da API para o mesmo IP configurado no `BASE_URL` da API.
3. O telemóvel/emulador tem de estar na mesma rede que a máquina onde a API corre.

### 3. Dashboard (`dashboard/`)
```bash
cd dashboard
npm install
npm run dev
```

### 4. Firebase Admin (`firebase-admin/`)
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
