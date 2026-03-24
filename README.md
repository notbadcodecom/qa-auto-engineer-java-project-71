# gendiff

### Hexlet tests and linter status:
[![Actions Status](https://github.com/notbadcodecom/qa-auto-engineer-java-project-71/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/notbadcodecom/qa-auto-engineer-java-project-71/actions)

### SonarQube Cloud statuses:
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=notbadcodecom_qa-auto-engineer-java-project-71&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=notbadcodecom_qa-auto-engineer-java-project-71)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=notbadcodecom_qa-auto-engineer-java-project-71&metric=bugs)](https://sonarcloud.io/summary/new_code?id=notbadcodecom_qa-auto-engineer-java-project-71)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=notbadcodecom_qa-auto-engineer-java-project-71&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=notbadcodecom_qa-auto-engineer-java-project-71)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=notbadcodecom_qa-auto-engineer-java-project-71&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=notbadcodecom_qa-auto-engineer-java-project-71)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=notbadcodecom_qa-auto-engineer-java-project-71&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=notbadcodecom_qa-auto-engineer-java-project-71)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=notbadcodecom_qa-auto-engineer-java-project-71&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=notbadcodecom_qa-auto-engineer-java-project-71)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=notbadcodecom_qa-auto-engineer-java-project-71&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=notbadcodecom_qa-auto-engineer-java-project-71)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=notbadcodecom_qa-auto-engineer-java-project-71&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=notbadcodecom_qa-auto-engineer-java-project-71)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=notbadcodecom_qa-auto-engineer-java-project-71&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=notbadcodecom_qa-auto-engineer-java-project-71)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=notbadcodecom_qa-auto-engineer-java-project-71&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=notbadcodecom_qa-auto-engineer-java-project-71)

### Описание
Консольная утилита для сравнения двух файлов и вывода различий между ними. 
Программа поддерживает форматы JSON и YAML изменений предоставляет несколько форматов вывода для отображения изменений

### Комманды
```
Usage: gendiff [-hV] [-f=format] filepath1 filepath2

Compares two configuration files and shows a difference.
      filepath1         path to first file
      filepath2         path to second file
  -f, --format=format   output format [default: stylish]
  -h, --help            Show this help message and exit.
  -V, --version         Print version information and exit.
```

### Формат вывода различий
  - **stylish** - наглядное представление с символами `+` и `-` для изменений (используется по умолчанию)
  - **plain** - текстовое описание изменений в читаемом формате
  - **json** - структурированный вывод в формате JSON

### Demo
[![asciicast](https://asciinema.org/a/865143.svg)](https://asciinema.org/a/865143)
