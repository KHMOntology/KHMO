# Foundational Model of Anatomy Ontology (FMA) Term Extraction

For FMA term extraction, we used [OBO ROBOT](https://robot.obolibrary.org/)

### Joint movements
```
robot extract --method STAR --input "fma.owl" -t "http://purl.org/sig/ont/fma/fma85817" -t "http://purl.org/sig/ont/fma/fma23139" -t "http://purl.org/sig/ont/fma/fma32432" -t "http://purl.org/sig/ont/fma/fma23864" --intermediates all --copy-ontology-annotations true --imports include -o fma_joint_extract.owl -vv
```

### anatomical parts
```
robot extract --method MIREOT --input "fma.owl" -u "http://purl.org/sig/ont/fma/fma62955" -L "parts.txt" --intermediates all --copy-ontology-annotations true --imports include -o fma_parts_extract.owl -vv
```

### merge for final FMA extraction file
```
robot merge --inputs "*_extract.owl" -o fma_khmo.owl --include-annotations true 
```