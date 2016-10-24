package me.shalvah.dbtest;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;



public class CourseListActivity extends AppCompatActivity implements LoaderManager
		.LoaderCallbacks<Cursor>
	{
		private ListView lv;
		private SimpleCursorAdapter sca;

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_course_list);

			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);

			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null)
			{
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
			lv = (ListView) findViewById(android.R.id.list);
			fillData();
		}

		private void fillData()
		{
			getLoaderManager().initLoader(0, null, this);

			sca = new SimpleCursorAdapter(this, R.layout.course_list_item, null,
					TestSimpleContentProvider.db.table(TestSimpleContentProvider.TABLE_COURSES).getAllColumns(), new int[]
					{R.id._id, R.id.code, R.id.title, R.id.description}, 0);
			lv.setAdapter(sca);

		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item)
		{
			int id = item.getItemId();
			switch (id)
			{
				case android.R.id.home:
					NavUtils.navigateUpFromSameTask(this);
					return true;
				case R.id.clear_all:
					getContentResolver().delete(TestSimpleContentProvider.contentUri("courses"), null, null);
					return true;

			}
			return super.onOptionsItemSelected(item);
		}


		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args)
		{
			String[] projection =
					TestSimpleContentProvider.db.table("courses").getAllColumns();

			return new CursorLoader(this, TestSimpleContentProvider.contentUri("courses"), projection, null, null,
					null);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data)
		{
			sca.swapCursor(data);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader)
		{
			sca.swapCursor(null);
		}

		public boolean onCreateOptionsMenu(Menu menu)
		{

			MenuInflater mi = getMenuInflater();
			mi.inflate(R.menu.menu_list, menu);
			return true;
		}

		public void add(View view)
		{
			String text = ((EditText) findViewById(R.id.inputCourseET)).getText().toString();
			String[] data = text.split(",");
			ContentValues values = new ContentValues();
			try
			{
				values.put("title", data[0]);
				values.put("code", data[1]);
				values.put("units", data[2]);
				values.put("description", data[3]);

			} catch (Exception e)
			{
				e.printStackTrace();
			}
			getContentResolver().insert(TestSimpleContentProvider.contentUri("courses"), values);
			((EditText) findViewById(R.id.inputCourseET)).setText("");
		}

		public void clear(View view)
		{
			String text = ((EditText) findViewById(R.id.inputCourseET)).getText().toString();
			if (text.equalsIgnoreCase(""))
			{
				getContentResolver().delete(TestSimpleContentProvider.contentUri("courses"), null, null);
			} else
			{
				getContentResolver().delete(
						Uri.withAppendedPath(TestSimpleContentProvider.contentUri
								(TestSimpleContentProvider.TABLE_COURSES), "/" + text),
						null,
						null);
			}
		}
	}