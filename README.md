# InvestFácil - Backend

[![CI](https://github.com/minhasnozes/investfacil-backend/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/minhasnozes/investfacil-backend/actions/workflows/ci.yml)

## Fluxo de branches

- `feature/nome_da_feature`: desenvolvimento de cada funcionalidade (commits diretos só aqui)
- `develop`: integração das features, via Pull Request
- `master`: versão estável, via Pull Request a partir da `develop`

O CI (GitHub Actions) roda compilação e testes a cada push e em todo Pull Request para `develop` e `master`.
