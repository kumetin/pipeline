# The Aqua case of the Data Lake

This project implements a simple pipeline mechanism.
It includes
- Parallel handling of multiple json array files, of multiple schemas which are source from multiple directories.
- Processing of existing files as well watching the directories for any new files which may be placed during the time the process is running.
- Reconciling of the data coming from different files in different times into single global records.
- An HTTP web hook to trigger exportation of the currently reconciled records.

## Getting Started
1. Clone the repository: git clone https://github.com/kumetin/pipeline.git
2. Navigate to the project directory: `cd pipeline`
3. Run the application: `mvn spring-boot:run`

This project includes initial input data populated in the following structure:

```
/
|-- input/
|   |-- connections/
|       |-- connections.json
|   |-- resources/
|       |-- resources.json
|   |-- scans/
|       |-- scans.json
```
Upon start, the application will start consuming the files under `input/`.
Any new files added into one of these directories is automatically intercepted and processed.
## REST API

### Export reconciled resources

**Endpoint:** `GET /api/export`

**Description:** Sends a request to server to export the currently processed reconciled records to local file system. The output is written into `output/` directory

**Response:**
- Status code: 200 OK

By default, the application runs on http://localhost:8080. You can test the API using your preferred REST client.

## Implementations
### `org.kmt.pipeline`
The place where the framework for composing a pipeline resides.
### `org.kmt.aqua`
The place where this application logic resides - `AquaPipeline` and the web server wrapped around it.
In order to build a pipeline, one must provide one or more tuples of a `DirectoryFileStreamListener` with a corresponding logic to handle the payload arriving to that directory, that is a `JsonElementHandler`.

In addition a reconciler instance must also be provided. This instance will save the global state of the data collected by all the streams. The reconciler exposes API for ingestion of all entities introduced through all the streams, and all the streams are provided with it inside their `handleEntity` hooks.

In the aqua pipeline case the `AquaReconciler` exposes the following:
- `processResource`
- `processConnection`
- `processScan`

In addition it also exposes currently reconciled resource entities:
- `getRepositories`
- `getImages`

So in order to downstream this pipeline results, we implement `AquaPipeline::exportToFile` by providing `JsonSerializer::writeJsonFile` with `AquaReconciler`'s `getRepositories` & `getImages`,

## Test
There's currently one sanity test `AquaPipelineTest`

Run the tests by running 
```mvn test```

## Configuration
Under `src/main/resource/application.yaml` you'll find properties which define the `input` and `output` directories for the pipeline

## Contributing
Feel free to contribute to this project by submitting issues or pull requests. Your feedback and contributions are highly appreciated.

## License
This project is licensed under the ZIBI License.
That means you're free to copy, duplicate, modify and wipe your bum with it cause it doesn't really worth much.

