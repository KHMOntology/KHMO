# README

We utilized [OBO ROBOT](https://robot.obolibrary.org/) to facilitate the merging of the ontologies

```
robot merge --input "khmo.owl" --input "fma_khmo.owl" --input "nbo_khmo.owl" --input "obi_khmo.owl" annotate --ontology-iri "http://w3id.org/khmo" --output results/khmo.owl
```